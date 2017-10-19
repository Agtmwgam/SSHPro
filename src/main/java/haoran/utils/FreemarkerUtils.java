package haoran.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class FreemarkerUtils {

	@SuppressWarnings("deprecation")
	public static void createDocByTemplate(ServletContext context, String templatePath, String wordFileName, Map<String, Object> dataModel){
		Configuration configuration = new Configuration();
		configuration.setEncoding(Locale.CHINA, "UTF-8");
		
		try {
			configuration.setServletContextForTemplateLoading(context, "/resources/templet/xml");
			Template template = configuration.getTemplate(templatePath);
			File file = new File(wordFileName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			template.process(dataModel, out);
			out.flush();
			out.close();
		} catch (TemplateNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
