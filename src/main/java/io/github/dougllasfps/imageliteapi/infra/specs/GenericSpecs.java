package io.github.dougllasfps.imageliteapi.infra.specs;

import org.springframework.data.jpa.domain.Specification;

public class GenericSpecs {

    // CORREÇÃO: O nome do construtor privado foi corrigido para bater exatamente com a classe
    private GenericSpecs(){

    }

    /**
     * Cria uma especificação genérica de conjunção (Equivale ao "WHERE 1 = 1").
     * O uso do tipo genérico <T> permite que este método seja reaproveitado
     * por qualquer repositório de qualquer entidade do sistema.
     */
    public static <T> Specification<T> conjunction(){
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }
}
