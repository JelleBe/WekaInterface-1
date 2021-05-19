package nl.bioinf.wekainterface.webcontrol;

import nl.bioinf.wekainterface.model.DataReader;
import nl.bioinf.wekainterface.model.LabelCounter;
import nl.bioinf.wekainterface.model.WekaClassifier;
import nl.bioinf.wekainterface.service.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author Marijke Eggink, Jelle Becirspahic
 */

@Controller
public class ExplorerController {
    @Value("${example.data.path}")
    private String exampleFilesFolder;

    @Autowired
    private DataReader dataReader;
    @Autowired
    private LabelCounter labelCounter;
    @Autowired
    private WekaClassifier wekaClassifier;
    @Autowired
    private ClassificationService classificationService;

    @GetMapping(value = "/explorer")
    public String getClassifierFormPage(Model model){
        List<String> classifierNames = wekaClassifier.getClassifierNames();
        List<String> filenames = dataReader.getDataSetNames();
        model.addAttribute("classifierNames", classifierNames);
        model.addAttribute("filenames", filenames);
        return "classifierForm";
    }

    @PostMapping(value = "/explorer")
    public String postClassifierFormPage(@RequestParam(name = "filename") String fileName,
                                         @RequestParam(name = "classifier") String classifierName,
                                         Model model, RedirectAttributes redirect) throws Exception {
        String arffFilePath = exampleFilesFolder + '/' + fileName;

        List<String> classify = classificationService.classify(arffFilePath, classifierName);

        redirect.addFlashAttribute("results", classify);
        return "redirect:/explorer/results";
    }

    @GetMapping(value = "/explorer/results")
    public String getResultsPage(Model model){
        return "results";
    }


    @GetMapping(value = "/test")
    public String plotWeatherData(Model model) throws IOException {
        String file = exampleFilesFolder + '/' + "weather.nominal.arff";
        labelCounter.readData(new File(file));
        labelCounter.setGroups();
        labelCounter.countLabels();
        model.addAttribute("data", labelCounter.mapToJSON());
        model.addAttribute("attributes", labelCounter.getAttributeArray());
        model.addAttribute("classLabel", labelCounter.getClassLabel());
        return "dataExplorer";
    }
}