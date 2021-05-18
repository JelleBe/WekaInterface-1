package nl.bioinf.wekainterface.webcontrol;

import nl.bioinf.wekainterface.model.DataReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ExplorerController {

    @Autowired
    private DataReader dataReader;

    @GetMapping(value = "/explorer")
    public String getClassifierFormPage(Model model){
        List<String> filenames = dataReader.getDataSetNames();
        model.addAttribute("filenames", filenames);
        return "classifierForm";
    }

    @PostMapping(value = "/explorer")
    public String postClassifierFormPage(HttpServletRequest request, RedirectAttributes redirect){
        String filename = request.getParameter("file-name");
        redirect.addFlashAttribute("filename", filename);
        return "redirect:/results";
    }

    @GetMapping(value = "/results")
    public String getResultsPage(Model model){
        String filename = (String) model.getAttribute("filename");
        model.addAttribute("filename", filename);
        return "results";
    }
}