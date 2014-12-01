package com.itextpdf.core.pdf;

import com.itextpdf.basics.PdfException;
import com.itextpdf.basics.io.ByteArrayOutputStream;

import java.io.IOException;


public class PdfObjectStream extends PdfStream {

    /**
     * Max number of objects in object stream.
     */
    public static final int maxObjStreamSize = 200;

    /**
     * Current object stream size (number of objects inside).
     */
    protected int size = 0;

    /**
     * Stream containing object indices, a heading part of object stream.
     */
    protected PdfOutputStream indexStream = new PdfOutputStream(new ByteArrayOutputStream());

    public PdfObjectStream(PdfDocument doc) {
        super(doc);
        put(PdfName.Type, PdfName.ObjStm);
        put(PdfName.N, new PdfNumber(size));
        put(PdfName.First, new PdfNumber(indexStream.getCurrentPos()));
    }

    /**
     * Adds object to the object stream.
     *
     * @param object object to add.
     * @throws PdfException
     */
    public void addObject(PdfObject object) throws PdfException {
        if (size == maxObjStreamSize) {
            //todo verify exception, possible size should be included in message
            throw new PdfException(PdfException.ObjectCannotBeAddedToObjectStream);
        }
        PdfOutputStream outputStream = getOutputStream();
        indexStream.writeInteger(object.getIndirectReference().getObjNr()).
                writeSpace().
                writeInteger(outputStream.getCurrentPos()).
                writeSpace();
        outputStream.write(object);
        object.getIndirectReference().setObjectStreamNumber(getIndirectReference().getObjNr());
        object.getIndirectReference().setIndex(size);
        outputStream.writeSpace();
        ((PdfNumber)get(PdfName.N)).setValue(++size);
        ((PdfNumber)get(PdfName.First)).setValue(indexStream.getCurrentPos());
    }

    /**
     * Gets object stream size (number of objects inside).
     *
     * @return object stream size.
     */
    public int getSize() {
        return size;
    }

    public PdfOutputStream getIndexStream() {
        return indexStream;
    }

    @Override
    protected void releaseContent() throws PdfException {
        super.releaseContent();
        try {
            indexStream.close();
        } catch (IOException e) {
            throw new PdfException(PdfException.IoException, e);
        }
        indexStream = null;
    }
}
