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
public class ItemBacklog {

    private Long idbacklog;
    private Projeto projeto;
    private Integer prioridade;
    private Integer estimativa;
    private StringBuffer estoria;
    private Date data_criacao;
    private boolean cancelado;

    public ItemBacklog(Projeto projeto) {
        this.idbacklog = null;
        this.projeto = projeto;
        this.prioridade = 0;
        this.estimativa = 0;
        this.estoria = new StringBuffer();
        this.data_criacao = new Date();
        this.cancelado = false;
    }

    /**
     * @return the idbacklog
     */
    public Long getIdbacklog() {
        return idbacklog;
    }

    /**
     * @param idbacklog the idbacklog to set
     */
    public void setIdbacklog(Long idbacklog) {
        this.idbacklog = idbacklog;
    }

    /**
     * @return the projeto
     */
    public Projeto getProjeto() {
        return projeto;
    }

    /**
     * @param projeto the projeto to set
     */
    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    /**
     * @return the prioridade
     */
    public Integer getPrioridade() {
        return prioridade;
    }

    /**
     * @param prioridade the prioridade to set
     */
    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }

    /**
     * @return the estimativa
     */
    public Integer getEstimativa() {
        return estimativa;
    }

    /**
     * @param estimativa the estimativa to set
     */
    public void setEstimativa(Integer estimativa) {
        this.estimativa = estimativa;
    }

    /**
     * @return the estoria
     */
    public StringBuffer getEstoria() {
        return estoria;
    }

    /**
     * @param estoria the estoria to set
     */
    public void setEstoria(StringBuffer estoria) {
        this.estoria = estoria;
    }

    /**
     * @return the data_criacao
     */
    public Date getData_criacao() {
        return data_criacao;
    }

    /**
     * @param data_criacao the data_criacao to set
     */
    public void setData_criacao(Date data_criacao) {
        this.data_criacao = data_criacao;
    }

    /**
     * @return the cancelado
     */
    public boolean isCancelado() {
        return cancelado;
    }

    /**
     * @param cancelado the cancelado to set
     */
    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }
    
}
