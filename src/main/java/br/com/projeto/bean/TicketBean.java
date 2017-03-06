package br.com.projeto.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import br.com.projeto.dao.DepartamentoDAO;
import br.com.projeto.dao.TicketDAO;
import br.com.projeto.domain.Departamento;
import br.com.projeto.domain.Ticket;
import br.com.projeto.domain.Usuario;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class TicketBean implements Serializable {
	private Ticket ticket;
	private AutenticacaoBean autenticacaoBean;
	private Usuario usuario;
	private FacesMessage message;
	private List<Departamento> departamentos;

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	public AutenticacaoBean getAutenticacaoBean() {
		return autenticacaoBean;
	}

	public void setAutenticacaoBean(AutenticacaoBean autenticacaoBean) {
		this.autenticacaoBean = autenticacaoBean;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@PostConstruct
	public void inicializar() {
		try {
			DepartamentoDAO departamentoDAO = new DepartamentoDAO();
			departamentos = departamentoDAO.listarAtendimento();

			novo();

		} catch (

		RuntimeException erro) {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um Erro ao Tentar Inicializar Tabelas.",
					"Erro: " + erro.getMessage());

			RequestContext.getCurrentInstance().showMessageInDialog(message);

			erro.printStackTrace();
		}
	}

	public void novo() {
		autenticacaoBean = Faces.getSessionAttribute("autenticacaoBean");
		usuario = autenticacaoBean.getUsuarioLogado();

		ticket = new Ticket();
		ticket.setUsuario(usuario);
		ticket.setDataAbertura(new java.util.Date());
		ticket.setUltimaInteracao(new java.util.Date());
		ticket.setStatus("Pendente");
		ticket.setPrioridade("3-Normal");
		ticket.setEmailEnviado(false);
	}

	public void menuPrincipal() {
		try {
			Faces.redirect("principal.xhtml");

		} catch (IOException erro) {
			erro.printStackTrace();
			Messages.addGlobalError("Não foi possível abrir o Menu Principal. Erro: " + erro.getMessage());
		}
	}

	public void salvar() {
		try {
			TicketDAO ticketDAO = new TicketDAO();
			ticket.setEmailEnviado(false);
			ticketDAO.merge(ticket);

			novo();

			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage("Ticket Aberto com Sucesso!",
					"Para acompanhar o andamento de seu Ticket acesse o menu Meus Ticket's"));

		} catch (

		RuntimeException erro) {
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null,
					new FacesMessage("Erro Abrir Ticket!", "Por favor entrar em contato com Suporte Ramal: 853"));

			erro.printStackTrace();
		}
	}
}
