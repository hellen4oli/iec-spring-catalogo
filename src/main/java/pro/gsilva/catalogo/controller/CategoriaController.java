package pro.gsilva.catalogo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pro.gsilva.catalogo.model.Categoria;
import pro.gsilva.catalogo.service.CategoriaService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

@Controller
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @RequestMapping(value = "/categorias", method = RequestMethod.GET)
    // @PreAuthorize("hasRole('ROLE_USER')")
    public ModelAndView getCategorias(@RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        ModelAndView mv = new ModelAndView("categorias");
        Page<Categoria> categorias = categoriaService.findAll(PageRequest.of(currentPage - 1, pageSize));
        mv.addObject("categorias", categorias);

        int totalPages = categorias.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            mv.addObject("pageNumbers", pageNumbers);
        }

        return mv;
    }

    @RequestMapping(value = "/add-categoria", method = RequestMethod.GET)
    public String getCategoriaForm(Categoria categoria) {
        return "categoriaForm";
    }

    @RequestMapping(value = "/edit-categoria/{id}", method = RequestMethod.GET)
    public ModelAndView getCategoriaForm(@PathVariable("id") long id) {
        ModelAndView model = new ModelAndView("categoriaForm");
        Categoria categoria = categoriaService.findById(id);

        model.addObject("categoria", categoria);
        return model;
    }

    @RequestMapping(value = "/categoria/edit/{id}", method = RequestMethod.GET)
    public ModelAndView getCAtegoriaFormEdit(@PathVariable("id") long id) {
        var model = new ModelAndView("categoriaForm");
        var categoria = categoriaService.findById(id);
        model.addObject("categoria", categoria);
        return model;
    }

    @RequestMapping(value = "/add-categoria", method = RequestMethod.POST)
    public ModelAndView salvarMusica(@Valid @ModelAttribute("categoria") Categoria musica,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            ModelAndView musicaForm = new ModelAndView("categoriaForm");
            musicaForm.addObject("mensagem", "Verifique os erros do formulário");
            return musicaForm;
        }

        categoriaService.save(musica);
        return new ModelAndView("redirect:/categorias");
    }

    @RequestMapping(value = "/del-categoria/{id}", method = RequestMethod.GET)
    public String delCategoria(@PathVariable("id") long id) {
        categoriaService.excluir(id);
        return "redirect:/categorias";
    }
}
