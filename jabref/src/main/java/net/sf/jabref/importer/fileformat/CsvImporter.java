package net.sf.jabref.importer.fileformat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jabref.importer.ImportFormatReader;
import net.sf.jabref.importer.OutputPrinter;
import net.sf.jabref.model.entry.BibEntry;

public class CsvImporter extends ImportFormat {




    /**
     * Return the name of this import format.
     */
    @Override
    public String getFormatName() {
        return "Csv";
    }

    /*
     *  (non-Javadoc)
     * @see net.sf.jabref.imports.ImportFormat#getCLIId()
     */
    @Override
    public String getCLIId() {
        return "csv";
    }

    /**
     * Check whether the source is in the correct format for this importer.
     */
    @Override
    public boolean isRecognizedFormat(InputStream in) throws IOException {
        return true;
    }

    /**
     * Parse the entries in the source, and return a List of BibEntry
     * objects.
     */
    @Override
    public List<BibEntry> importEntries(InputStream stream, OutputPrinter status) throws IOException {

        String splitBy = ",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))";

        List<BibEntry> bibItems = new ArrayList<>();
        BufferedReader in = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(stream));
        String line;
        Map<String, String> hm = new HashMap<>();
        Map<String, StringBuilder> lines = new HashMap<>();
        StringBuilder previousLine = null;
        String[] entriesFields = null;
        boolean isFirstLine = true;
        //#TODO: implementar logica de importacao

        while ((line = in.readLine()) != null) {
            String type = null;
            if (line.isEmpty()) {
                continue; // ignore empty lines, e.g. at file
            }


            String[] entries = line.split(splitBy); // use comma as separator
            System.out.println(Arrays.toString(entries));
            System.out.println(entries.length);


            if (isFirstLine) {
                entriesFields = entries;
                isFirstLine = false;
            } else {
                for (int i = 0; i < entriesFields.length; i++) {
                    String val = entries[i];

                    if ("BibliographyType".equals(entriesFields[i])) {
                        if ("7".equals(val)) {

                            val = "article";
                            type = val;
                        }
                        if ("1".equals(val)) {
                            val = "book";
                            type = val;
                        }
                    }
                    hm.put(entriesFields[i], val);

                }
            }

            BibEntry b = new BibEntry(DEFAULT_BIBTEXENTRY_ID, type); // id assumes an existing database so don't
            // create one here
            b.setField(hm);
            if (!b.getFieldNames().isEmpty()) {
                bibItems.add(b);
            }

        }
        return bibItems;

    }
}