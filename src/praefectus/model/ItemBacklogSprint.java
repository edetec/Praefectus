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
public class ItemBacklogSprint {

    private Long id;
    private Sprint sprint;
    private ItemBacklog item;
    private Status status;
    private Date dataConclusao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public ItemBacklog getItem() {
        return item;
    }

    public void setItem(ItemBacklog item) {
        this.item = item;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(Date dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
}
