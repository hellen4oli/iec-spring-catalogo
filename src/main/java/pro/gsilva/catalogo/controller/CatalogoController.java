package pro.gsilva.catalogo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;

import pro.gsilva.catalogo.model.Categoria;
import pro.gsilva.catalogo.model.Musica;
import pro.gsilva.catalogo.service.CatalogoService;
import pro.gsilva.catalogo.service.CategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @Autowired
    private CategoriaService categoriaService;

    @RequestMapping(value = "/musicas", method = RequestMethod.GET)
    public ModelAndView getMusicas() {
        ModelAndView mv = new ModelAndView("musicas");
        List<Musica> musicas = catalogoService.findAll();
        List<Categoria> categorias = categoriaService.findAll();
        mv.addObject("musicas", musicas);
        mv.addObject("categorias", categorias);
        return mv;
    }

    @RequestMapping(value = "/musicas/{id}", method = RequestMethod.GET)
    public ModelAndView getMusicaDetalhes(@PathVariable("id") long id) {
        ModelAndView mv = new ModelAndView("musicaDetalhes");
        Musica musica = catalogoService.findById(id);
        mv.addObject("musica", musica);
        return mv;
    }

    @RequestMapping(value = "/musicas/edit/{id}", method = RequestMethod.GET)
    public ModelAndView getFormEdit(@PathVariable("id") long id) {
        ModelAndView model = new ModelAndView("musicaForm");
        Musica musica = catalogoService.findById(id);
        List<Categoria> listCategoria = categoriaService.findAll();
        model.addObject("categorias", listCategoria);
        model.addObject("musica", musica);
        return model;
    }

    @RequestMapping(value = "/addMusica", method = RequestMethod.GET)
    public ModelAndView getMusicaForm(Musica musica) {
        ModelAndView model = new ModelAndView("musicaForm");
        List<Categoria> listCategoria = categoriaService.findAll();
        model.addObject("categorias", listCategoria);
        return model;
    }

    @RequestMapping(value = "/addMusica", method = RequestMethod.POST)
    public ModelAndView salvarMusica(@Valid @ModelAttribute("musica") Musica musica,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            ModelAndView musicaForm = new ModelAndView("musicaForm");
            musicaForm.addObject("mensagem", "Verifique os erros do formulário");
            return musicaForm;
        }
        musica.setData(LocalDate.now());
        catalogoService.save(musica);
        return new ModelAndView("redirect:/musicas");
    }

    @GetMapping("/musicas/pesquisar")
    public ModelAndView pesquisarPorCategoria(
            @RequestParam("categoria") Long categoria, @RequestParam("titulo") String titulo) {
        ModelAndView model = new ModelAndView("musicas");
        List<Musica> musicas = new ArrayList<>();
        if (categoria != null && categoria != 0)
            musicas = catalogoService.findByCategoria(categoria);

        else if (titulo != null)
            musicas = catalogoService.findByTitulo(titulo);

        List<Categoria> listCategoria = categoriaService.findAll();
        model.addObject("categorias", listCategoria);

        model.addObject("musicas", musicas);
        return model;
    }

    @RequestMapping(value = "/delMusica/{id}", method = RequestMethod.GET)
    public String delMusica(@PathVariable("id") long id) {
        catalogoService.excluir(id);
        return "redirect:/musicas";
    }

}
