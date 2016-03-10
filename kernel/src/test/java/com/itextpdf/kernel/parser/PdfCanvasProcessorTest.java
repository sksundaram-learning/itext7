package com.itextpdf.kernel.parser;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.IntegrationTest;
import com.itextpdf.test.ExtendedITextTest;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IntegrationTest.class)
public class PdfCanvasProcessorTest extends ExtendedITextTest {

    public static final String sourceFolder = "./src/test/resources/com/itextpdf/kernel/parser/PdfCanvasProcessorTest/";

    @Test
    public void contentStreamProcessorTest() throws IOException {

        PdfDocument document = new PdfDocument(new PdfReader(sourceFolder + "yaxiststar.pdf"), new PdfWriter(new ByteArrayOutputStream()));

        for (int i = 1; i <= document.getNumberOfPages(); ++i) {
            PdfPage page = document.getPage(i);

            PdfCanvasProcessor processor = new PdfCanvasProcessor(new EventListener() {
                public void eventOccurred(EventData data, EventType type) {
                    switch (type) {
                        case BEGIN_TEXT:
                            System.out.println("-------- BEGIN TEXT CALLED ---------");
                            System.out.println("------------------------------------");
                            break;

                        case RENDER_TEXT:
                            System.out.println("-------- RENDER TEXT CALLED --------");

                            TextRenderInfo renderInfo = (TextRenderInfo) data;
                            System.out.println("String: " + renderInfo.getPdfString());

                            System.out.println("------------------------------------");
                            break;

                        case END_TEXT:
                            System.out.println("-------- END TEXT CALLED -----------");
                            System.out.println("------------------------------------");
                            break;

                        case RENDER_IMAGE:
                            System.out.println("-------- RENDER IMAGE CALLED---------");

                            ImageRenderInfo renderInfo1 = (ImageRenderInfo) data;
                            System.out.println("Image: " + renderInfo1.getImage().getPdfObject());

                            System.out.println("------------------------------------");
                            break;

                        case RENDER_PATH:
                            System.out.println("-------- RENDER PATH CALLED --------");

                            PathRenderInfo renderinfo2 = (PathRenderInfo) data;
                            System.out.println("Path: " + renderinfo2.getPath());

                            System.out.println("------------------------------------");
                            break;

                        case CLIP_PATH_CHANGED:
                            System.out.println("-------- CLIPPING PATH CALLED-------");

                            ClippingPathInfo renderinfo3 = (ClippingPathInfo) data;
                            System.out.println("Clipping path: " + renderinfo3.getClippingPath());

                            System.out.println("------------------------------------");
                            break;
                    }
                }

                public Set<EventType> getSupportedEvents() {
                    return null;
                }
            });

            processor.processPageContent(page);

        }
    }

}