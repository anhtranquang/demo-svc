package com.finx.contractservice.infra.client.pdfbox;

import com.finx.contractservice.infra.client.pdfbox.exception.GenerateFileException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class PdfBoxClient {

    private static final ClassPathResource FONT_RESOURCE =
            new ClassPathResource("static/font/TimesNewRoman.ttf");
    private static final String FONT_NAME = "TimesNewRomanPSMT";
    private static final Pattern DEFAULT_PATTERN = Pattern.compile("(\\w+)\\s.*", Pattern.MULTILINE);

    public OutputStream generatePdf(byte[] template, Map<String, String> data) throws IOException {
        PDDocument pdfDocument = PDDocument.load(template);
        PDFont font = PDType0Font.load(pdfDocument, FONT_RESOURCE.getInputStream());
        PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();

        if (acroForm == null) {
            throw new NullPointerException();
        }

        PDResources res = acroForm.getDefaultResources();
        res.put(COSName.getPDFName(FONT_NAME), font);
        acroForm.setDefaultResources(res);

        insertDataToTemplate(acroForm, data);

        acroForm.flatten();

        try (OutputStream outputStream = new ByteArrayOutputStream()) {
            pdfDocument.save(outputStream);
            return outputStream;
        } finally {
            pdfDocument.close();
        }
    }

    private String createAppearance(String defaultAppearance, String fontName) {

        // replace font name in default appearance string
        Matcher matcher = DEFAULT_PATTERN.matcher(defaultAppearance);
        while (matcher.find()) {
            String oldFontName = matcher.group(1);
            defaultAppearance = defaultAppearance.replaceFirst(oldFontName, fontName);
        }

        return defaultAppearance;
    }

    private PDTextField setFieldValue(PDTextField field, String fieldName, String fieldValue)
            throws IOException {

        if (Objects.isNull(field)) {
            throw new GenerateFileException(
                    String.format("Field %s is not found in template", fieldName));
        }

        String da = field.getDefaultAppearance();
        field.setDefaultAppearance(createAppearance(da, FONT_NAME));
        field.setValue(fieldValue);
        return field;
    }

    private List<PDField> insertDataToTemplate(PDAcroForm acroForm, Map<String, String> data)
            throws IOException {

        List<PDField> fields = new ArrayList<>();
        for (Iterator<PDField> pdfFields = acroForm.getFieldIterator(); pdfFields.hasNext(); ) {
            PDTextField field = (PDTextField) pdfFields.next();
            String key = field.getFullyQualifiedName();
            fields.add(setFieldValue(field, key, data.getOrDefault(key, "")));
        }

        return fields;
    }
}
