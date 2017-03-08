package br.com.projeto.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import br.com.projeto.dao.TicketDAO;
import br.com.projeto.domain.Ticket;

@SuppressWarnings("serial")
@ViewScoped
@ManagedBean
public class resumoEquipeBean implements Serializable {
	private List<Ticket> tickets;
	private FacesMessage message;

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	@PostConstruct
	public void inicializar() {
		resumoEquipe();
	}

	@SuppressWarnings("unchecked")
	public void resumoEquipe() {
		try {
			TicketDAO ticketDAO = new TicketDAO();
			tickets = ticketDAO.resumoDepartamento();

		} catch (

		RuntimeException erro) {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um Erro ao Tentar Listar Resumo.",
					"Erro: " + erro.getMessage());

			RequestContext.getCurrentInstance().showMessageInDialog(message);

			erro.printStackTrace();
		}

	}
}
