package br.ufms.jogai.data.firebase;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Default methods for extracting typed field values from a Firestore {@link DocumentSnapshot}.
 *
 * <p>Implement this interface to gain access to {@code required*} and {@code optional*} helpers
 * that validate presence, convert Firestore types to Java types, and throw descriptive exceptions
 * when a required field is missing.
 *
 * <p>All methods are pure read operations on the snapshot — they carry no state and are safe
 * to call from any context that has a {@link DocumentSnapshot}.
 */
public interface DocumentSnapshotExtractors {

    // ------------------------------------------------------------------
    //  Required field extractors
    // ------------------------------------------------------------------

    /**
     * Reads a required Long field from a document snapshot.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path (dot-separated for nested fields)
     * @return the long value
     * @throws NoSuchElementException if the field is missing or null
     */
    default long requiredLong(DocumentSnapshot doc, String field) {
        Long value = doc.getLong(field);
        if (value == null) throw new NoSuchElementException("Required field is missing: " + field);
        return value;
    }

    /**
     * Reads a required int value stored as a Long field.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the int value
     * @throws NoSuchElementException if the field is missing or null
     */
    default int requiredInt(DocumentSnapshot doc, String field) {
        return (int) requiredLong(doc, field);
    }

    /**
     * Reads a required Double field from a document snapshot.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the double value
     * @throws NoSuchElementException if the field is missing or null
     */
    default double requiredDouble(DocumentSnapshot doc, String field) {
        Double value = doc.getDouble(field);
        if (value == null) throw new NoSuchElementException("Required field is missing: " + field);
        return value;
    }

    /**
     * Reads a required Boolean field from a document snapshot.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the boolean value
     * @throws NoSuchElementException if the field is missing or null
     */
    default boolean requiredBoolean(DocumentSnapshot doc, String field) {
        Boolean value = doc.getBoolean(field);
        if (value == null) throw new NoSuchElementException("Required field is missing: " + field);
        return value;
    }

    /**
     * Reads a required String field from a document snapshot.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the non-null string value
     * @throws NoSuchElementException if the field is missing or null
     */
    default String requiredString(DocumentSnapshot doc, String field) {
        String value = doc.getString(field);
        if (value == null) throw new NoSuchElementException("Required field is missing: " + field);
        return value;
    }

    /**
     * Reads a required Timestamp field and converts it to {@link Instant}.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the non-null instant value
     * @throws NoSuchElementException if the field is missing or null
     */
    default Instant requiredInstant(DocumentSnapshot doc, String field) {
        Timestamp value = doc.getTimestamp(field);
        if (value == null) throw new NoSuchElementException("Required field is missing: " + field);
        return toInstant(value);
    }

    /**
     * Reads a required Timestamp field and converts it to {@link LocalDate} in UTC.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the non-null local date value
     * @throws NoSuchElementException if the field is missing or null
     */
    default LocalDate requiredLocalDate(DocumentSnapshot doc, String field) {
        Timestamp value = doc.getTimestamp(field);
        if (value == null) throw new NoSuchElementException("Required field is missing: " + field);
        return toInstant(value).atZone(ZoneOffset.UTC).toLocalDate();
    }

    /**
     * Reads a required Timestamp field and converts it to {@link java.time.LocalDateTime} in UTC.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the non-null local date time value
     * @throws NoSuchElementException if the field is missing or null
     */
    default java.time.LocalDateTime requiredLocalDateTime(DocumentSnapshot doc, String field) {
        Timestamp value = doc.getTimestamp(field);
        if (value == null) throw new NoSuchElementException("Required field is missing: " + field);
        return toInstant(value).atZone(ZoneOffset.UTC).toLocalDateTime();
    }

    /**
     * Parses the document ID as a required UUID.
     *
     * @param doc the document snapshot
     * @return the non-null UUID
     * @throws NoSuchElementException if the document ID is empty
     * @throws IllegalArgumentException if the document ID is not a valid UUID
     */
    default UUID requiredUUID(DocumentSnapshot doc) {
        String id = doc.getId();
        if (id.isEmpty()) throw new NoSuchElementException("Document ID is empty");
        return UUID.fromString(id);
    }

    /**
     * Reads a single {@link DocumentReference} from a field.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the non-null document reference
     * @throws NoSuchElementException if the field is missing or null
     */
    default DocumentReference requiredReference(DocumentSnapshot doc, String field) {
        Object value = doc.get(field);
        if (value == null) throw new NoSuchElementException("Required field is missing: " + field);
        if (value instanceof DocumentReference ref) return ref;
        throw new IllegalStateException("Field is not a DocumentReference: " + field);
    }

    /**
     * Reads a required Map (nested object) field.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the non-null map
     * @throws NoSuchElementException if the field is missing or null
     */
    @SuppressWarnings("unchecked")
    default Map<String, Object> requiredMap(DocumentSnapshot doc, String field) {
        Object value = doc.get(field);
        if (value == null) throw new NoSuchElementException("Required field is missing: " + field);
        if (value instanceof Map<?, ?> map) return (Map<String, Object>) map;
        throw new IllegalStateException("Field is not a Map: " + field);
    }

    // ------------------------------------------------------------------
    //  Optional field extractors (return null when absent)
    // ------------------------------------------------------------------

    /**
     * Reads an optional Long field.
     * Returns {@code null} when the field is absent.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the long value, or {@code null} if absent
     */
    default Long optionalLong(DocumentSnapshot doc, String field) {
        return doc.getLong(field);
    }

    /**
     * Reads an optional int value stored as a Long field.
     * Returns {@code null} when the field is absent.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the int value, or {@code null} if absent
     */
    default Integer optionalInt(DocumentSnapshot doc, String field) {
        Long value = doc.getLong(field);
        return value == null ? null : value.intValue();
    }

    /**
     * Reads an optional Double field.
     * Returns {@code null} when the field is absent.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the double value, or {@code null} if absent
     */
    default Double optionalDouble(DocumentSnapshot doc, String field) {
        return doc.getDouble(field);
    }

    /**
     * Reads an optional Boolean field.
     * Returns {@code null} when the field is absent.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the boolean value, or {@code null} if absent
     */
    default Boolean optionalBoolean(DocumentSnapshot doc, String field) {
        return doc.getBoolean(field);
    }

    /**
     * Reads an optional String field.
     * Returns {@code null} when the field is absent.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the string value, or {@code null} if absent
     */
    default String optionalString(DocumentSnapshot doc, String field) {
        return doc.getString(field);
    }

    /**
     * Reads an optional Timestamp field and converts it to {@link Instant}.
     * Returns {@code null} when the field is absent.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the instant value, or {@code null} if absent
     */
    default Instant optionalInstant(DocumentSnapshot doc, String field) {
        Timestamp value = doc.getTimestamp(field);
        return value == null ? null : toInstant(value);
    }

    /**
     * Reads an optional Timestamp field and converts it to {@link LocalDate} in UTC.
     * Returns {@code null} when the field is absent.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the local date value, or {@code null} if absent
     */
    default LocalDate optionalLocalDate(DocumentSnapshot doc, String field) {
        Timestamp value = doc.getTimestamp(field);
        return value == null ? null : toInstant(value).atZone(ZoneOffset.UTC).toLocalDate();
    }

    /**
     * Reads an optional Timestamp field and converts it to {@link java.time.LocalDateTime} in UTC.
     * Returns {@code null} when the field is absent.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the local date time value, or {@code null} if absent
     */
    default java.time.LocalDateTime optionalLocalDateTime(DocumentSnapshot doc, String field) {
        Timestamp value = doc.getTimestamp(field);
        return value == null ? null : toInstant(value).atZone(ZoneOffset.UTC).toLocalDateTime();
    }

    /**
     * Reads an optional single {@link DocumentReference} from a field.
     * Returns {@code null} when the field is absent.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the document reference, or {@code null} if absent
     */
    default DocumentReference optionalReference(DocumentSnapshot doc, String field) {
        Object value = doc.get(field);
        if (value instanceof DocumentReference ref) return ref;
        if (value == null) return null;
        throw new IllegalStateException("Field is not a DocumentReference: " + field);
    }

    /**
     * Reads an optional Map (nested object) field.
     * Returns {@code null} when the field is absent.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return the map, or {@code null} if absent
     */
    @SuppressWarnings("unchecked")
    default Map<String, Object> optionalMap(DocumentSnapshot doc, String field) {
        Object value = doc.get(field);
        if (value instanceof Map map) return (Map<String, Object>) map;
        if (value == null) return null;
        throw new IllegalStateException("Field is not a Map: " + field);
    }

    /**
     * Reads an optional array of scalar values from a field.
     * Returns an empty list when the field is absent or null.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return a non-null list of values (maybe empty)
     * @throws IllegalStateException if the field exists but is not a list
     */
    default List<Object> optionalArray(DocumentSnapshot doc, String field) {
        Object value = doc.get(field);
        if (value == null) return List.of();
        if (value instanceof List<?> list) {
            return List.copyOf(list);
        }
        throw new IllegalStateException("Field is not a list: " + field);
    }

    /**
     * Reads a required list of {@link DocumentReference} values from a field.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return a non-null list of document references
     * @throws NoSuchElementException if the field is missing or null
     * @throws IllegalStateException  if the field exists but is not a list
     */
    default List<DocumentReference> requiredDocumentRefs(DocumentSnapshot doc, String field) {
        Object value = doc.get(field);
        if (value == null) throw new NoSuchElementException("Required field is missing: " + field);
        if (value instanceof List<?> list) {
            return list.stream().map(DocumentReference.class::cast).toList();
        }
        throw new IllegalStateException("Field is not a list: " + field);
    }

    /**
     * Reads an optional list of {@link DocumentReference} values from a field.
     * Returns an empty list when the field is absent or null.
     *
     * @param doc   the document snapshot
     * @param field the non-null field path
     * @return a non-null list of document references (may be empty)
     * @throws IllegalStateException if the field exists but is not a list
     */
    default List<DocumentReference> optionalDocumentRefs(DocumentSnapshot doc, String field) {
        Object value = doc.get(field);
        if (value == null) return List.of();
        if (value instanceof List<?> list) {
            return list.stream().map(DocumentReference.class::cast).toList();
        }
        throw new IllegalStateException("Field is not a list: " + field);
    }

    /**
     * Parses the document ID as an optional UUID.
     * Returns {@code null} when the document ID is empty.
     *
     * @param doc the document snapshot
     * @return the UUID, or {@code null} if the ID is empty
     * @throws IllegalArgumentException if the document ID is not a valid UUID
     */
    default UUID optionalUUID(DocumentSnapshot doc) {
        String id = doc.getId();
        return id.isEmpty() ? null : UUID.fromString(id);
    }

    // ------------------------------------------------------------------
    //  Internal conversion helpers
    // ------------------------------------------------------------------

    /**
     * Converts a Firestore {@link Timestamp} to a Java {@link Instant}.
     *
     * @param timestamp the non-null timestamp
     * @return the equivalent instant
     */
    static Instant toInstant(Timestamp timestamp) {
        Objects.requireNonNull(timestamp, "Timestamp must not be null");
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
}
