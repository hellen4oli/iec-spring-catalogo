package pro.gsilva.catalogo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.gsilva.catalogo.model.Categoria;

public interface CategoriaService {
    Page<Categoria> findAll(Pageable pageable);

    List<Categoria> findAll();

    public Categoria findById(Long id);

    public Categoria save(Categoria cateogira);

    public void excluir(long id);
}
