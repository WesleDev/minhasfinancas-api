package com.wesledev.minhasFinancas.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.wesledev.minhasFinancas.exception.RegraNegocioException;
import com.wesledev.minhasFinancas.model.entity.Lancamento;
import com.wesledev.minhasFinancas.model.entity.Usuario;
import com.wesledev.minhasFinancas.model.enums.StatusLancamento;
import com.wesledev.minhasFinancas.model.repository.LancamentoRepository;
import com.wesledev.minhasFinancas.model.repository.LancamentoRepositoryTest;
import com.wesledev.minhasFinancas.service.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@SpyBean
	LancamentoServiceImpl service;

	@MockBean
	LancamentoRepository repository;

	@Test
	public void hasSaveAnLaunch() {
		// cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.createLaunch();
		Mockito.doNothing().when(service).validar(lancamentoASalvar);

		Lancamento lancamentoSalvo = LancamentoRepositoryTest.createLaunch();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

		// execucao
		Lancamento lancamento = service.salvar(lancamentoASalvar);

		// verificacao
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}

	@Test
	public void mustNotSaveAnLaunchWhenExistsErrorValidation() {
		// cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.createLaunch();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);

		// execucao e verificacao
		Assertions.catchThrowableOfType(() -> service.salvar(lancamentoASalvar), RegraNegocioException.class);

		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}

	@Test
	public void hasRefreshAnLaunch() {
		// cenario
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.createLaunch();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);

		Mockito.doNothing().when(service).validar(lancamentoSalvo);

		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

		// execucao
		service.atualizar(lancamentoSalvo);

		// verificacao
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
	}

	@Test
	public void throwErrorToRefreshAnLauncherHasNotBeenSaved() {
		// cenario
		Lancamento lancamento = LancamentoRepositoryTest.createLaunch();

		// execucao e verificacao
		Assertions.catchThrowableOfType(() -> service.atualizar(lancamento), NullPointerException.class);

		Mockito.verify(repository, Mockito.never()).save(lancamento);
	}

	@Test
	public void hasDeleteAnLaunch() {
		// cenario
		Lancamento lancamento = LancamentoRepositoryTest.createLaunch();
		lancamento.setId(1l);

		// execucao
		service.deletar(lancamento);

		// verificacao
		Mockito.verify(repository).delete(lancamento);
	}

	@Test
	public void throwErrorToDeleteAnLauncherHasNotBeenSaved() {
		// cenario
		Lancamento lancamento = LancamentoRepositoryTest.createLaunch();

		// execucao
		Assertions.catchThrowableOfType(() -> service.deletar(lancamento), NullPointerException.class);

		// verificacao
		Mockito.verify(repository, Mockito.never()).delete(lancamento);
	}

	@Test
	public void hasFilterLaunchers() {
		// cenario
		Lancamento lancamento = LancamentoRepositoryTest.createLaunch();
		lancamento.setId(1l);

		List<Lancamento> lista = Arrays.asList(lancamento);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);

		// execucao
		List<Lancamento> resultado = service.buscar(lancamento);

		// verificacoes
		Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
	}

	@Test
	public void hasRefreshStatusAnLauncher() {
		// cenario
		Lancamento lancamento = LancamentoRepositoryTest.createLaunch();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);

		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);

		// execucao
		service.atualizarStatus(lancamento, novoStatus);

		// verificacoes
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);
	}

	@Test
	public void getLaunchById() {
		// cenario
		Long id = 1l;

		Lancamento lancamento = LancamentoRepositoryTest.createLaunch();
		lancamento.setId(id);

		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));

		// execucao
		Optional<Lancamento> resultado = service.obterPorId(id);

		// verificacao
		Assertions.assertThat(resultado.isPresent()).isTrue();

	}

	@Test
	public void hasReturnEmptyWhenLaunchNotExists() {
		// cenario
		Long id = 1l;

		Lancamento lancamento = LancamentoRepositoryTest.createLaunch();
		lancamento.setId(id);

		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

		// execucao
		Optional<Lancamento> resultado = service.obterPorId(id);

		// verificacao
		Assertions.assertThat(resultado.isPresent()).isFalse();
	}

	@Test
	public void mustThrowErrorToValidateAnLauncher() {
		Lancamento lancamento = new Lancamento();

		// acao
		Throwable erro = Assertions.catchThrowable(() -> service.validar(lancamento));

		// verificacao
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Informe uma Descrição válida.");

		lancamento.setDescricao("");

		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Informe uma Descrição válida.");

		lancamento.setDescricao("salario");

		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

		lancamento.setMes(0);

		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

		lancamento.setMes(13);

		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

		lancamento.setMes(1);

		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");

		lancamento.setAno(202);

		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");

		lancamento.setAno(2022);

		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");

		lancamento.setUsuario(new Usuario());

		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");

		lancamento.getUsuario().setId(1l);

		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");

		lancamento.setValor(BigDecimal.ZERO);

		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");

		lancamento.setValor(BigDecimal.valueOf(1));
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Infome um Tipo de Lançamento.");
	}
}
