import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    protected Map<String, List<PageEntry>> wordIndexingStorage;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        wordIndexingStorage = IndexedStorage.getIndexedStorage().getStorage();
        File[] arrOfPdfs = new File("pdfs").listFiles();

        for (int i = 0; i < arrOfPdfs.length; i++) {
            var doc = new PdfDocument(new PdfReader(arrOfPdfs[i]));

            for (int j = 0; j < doc.getNumberOfPages(); j++) {
                var file = doc.getPage(j + 1);
                var text = PdfTextExtractor.getTextFromPage(file);
                String[] words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freques = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty() || word.length() <= 3) {
                        continue;
                    }
                    freques.put(word.toLowerCase(), freques.getOrDefault(word, 0) + 1);
                }

                String namePdfFile = doc.getDocumentInfo().getTitle();
                for (Map.Entry<String, Integer> entry : freques.entrySet()) {
                    String temporaryWord = entry.getKey();
                    int temporaryValue = entry.getValue();

                    List<PageEntry> temporaryListPage = new ArrayList<>();
                    temporaryListPage.add(new PageEntry(namePdfFile, j + 1, temporaryValue));
                    if (wordIndexingStorage.containsKey(temporaryWord)) {
                        wordIndexingStorage.get(temporaryWord).add(new PageEntry(namePdfFile, j + 1, temporaryValue));
                    } else {
                        wordIndexingStorage.put(temporaryWord, temporaryListPage);
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> pageEntries = wordIndexingStorage.getOrDefault(word.toLowerCase(), Collections.emptyList());
        Collections.sort(pageEntries);
        return pageEntries;
    }
}
