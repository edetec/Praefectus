/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.model;

import java.util.Date;

/**
 *
 * @author soares
 */
public class Projeto {
    private Long id;
    private String nome;
    private Integer duracaoSprint;
    private Integer pontosSprint;
    private StringBuffer descricao;
    private Date inicio;
    private Date fim;

    public Integer getDuracaoSprint() {
        return duracaoSprint;
    }

    public void setDuracaoSprint(int duracaoSprint) {
        this.duracaoSprint = duracaoSprint;
    }

    public Integer getPontosSprint() {
        return pontosSprint;
    }

    public void setPontosSprint(int pontosSprint) {
        this.pontosSprint = pontosSprint;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public StringBuffer getDescricao() {
        return descricao;
    }

    public void setDescricao(StringBuffer descricao) {
        this.descricao = descricao;
    }
}
