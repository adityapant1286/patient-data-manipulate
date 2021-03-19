package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides delimited file reader implementation.
 * A file {@link Path} will be an input and a {@link List} of
 * rows as an output. Columns in an each row will be added to
 * a list of String.
 *
 * @version 1.0
 * @since 1.0
 * Date: 27-feb-2021
 *
 */
public class CsvReaderImpl
        implements DelimitedReader<Path, List<List<String>>> {

    @Override
    public List<List<String>> read(Path path, boolean removeHeaders)
            throws IOException {

        final List<List<String>> data = this.read(path);

        if (removeHeaders && !data.isEmpty()) {
            data.remove(0);
        }
        return data;
    }

    /**
     * Reads a comma separated file
     *
     * @param path Csv file path
     * @return A list of rows and columns
     * @throws IOException If file does not exists or inaccessible
     */
    private List<List<String>> read(Path path)
            throws IOException {
        Objects.requireNonNull(path, "Invalid CSV file path");

        return Files.lines(path)
                    .map(line -> Arrays.asList(line.split(DEFAULT_DELIMITER)))
                    .collect(Collectors.toList());

    }

}
