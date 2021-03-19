package io;

import java.io.IOException;

/**
 * A contract for reading a file delimited by a separator.
 * Default delimiter comma (,).
 * Candidate for functional interface.
 *
 * @param <Input> A file path or URI
 * @param <Output> File contents

 * @version 1.0
 * @since 1.0
 * Date: 27-feb-2021
 */
@FunctionalInterface
public interface DelimitedReader<Input, Output> {
    /**
     * Default delimiter for comma separator data file
     */
    String DEFAULT_DELIMITER = ",";

    /**
     * Reads a delimited file and return file contents.
     *
     * @param path File path or URI
     * @param removeHeaders If {@code true} the headers in input file will be removed before
     *                      returning output.
     *                      <b>Note:</b> The first row will be considered as header row
     * @return File contents
     * @throws IOException If file does not exists or inaccessible
     */
    Output read(Input path, boolean removeHeaders) throws IOException;
}
