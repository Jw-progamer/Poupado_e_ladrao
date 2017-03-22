package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Ladrao extends ProgramaLadrao {

	public Ladrao() {
		super();
		pesos = new Peso();
	}

	public class Peso {
		public Map<Point, Integer> posicoesPeso;

		public Peso() {
			posicoesPeso = new HashMap<>();
		}

		public int getPeso(Point posicao) {
			if (!posicoesPeso.containsKey(posicao)) {
				posicoesPeso.put(posicao, 0);
			}

			return posicoesPeso.get(posicao);
		}

		public int getPeso(int x, int y) {
			Point chave = new Point(x, y);
			return this.getPeso(chave);
		}

		public void aumetarPeso(Point posicao) {
			int peso = this.getPeso(posicao);
			posicoesPeso.replace(posicao, peso += 1);
		}
	}

	public Peso pesos;

	final int parado = 0;
	final int cima = 1;
	final int baixo = 2;
	final int esquerda = 4;
	final int direita = 3;

	final int verCima = 7;
	final int verBaixo = 16;
	final int verEsquerda = 11;
	final int verDireita = 12;

	final int[] norte = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	final int[] sul = { 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };
	final int[] oeste = { 0, 1, 5, 6, 10, 11, 14, 15, 19, 20 };
	final int[] leste = { 3, 4, 8, 9, 12, 13, 17, 18, 22, 23 };

	final int[] faroAoNorte = { 0, 1, 2 };
	final int[] faroAoSul = { 5, 6, 7 };
	final int[] faroAoOeste = { 0, 3, 5 };
	final int[] faroAoLeste = { 2, 4, 7 };

	final int semVisao = -2;
	final int foraMatriz = -1;
	final int caminho = 0;
	final int parede = 1;
	final int banco = 3;
	final int moeda = 4;
	final int pastilha = 5;
	final int poupador = 100;
	final int ladrao = 200;

	public int explorar() {

		List<Integer> acoesPossiveis = new ArrayList<>();
		List<Integer> persegisoesPossíveis = new ArrayList<>();
		Point posicaoAtual = sensor.getPosicao();
		int[] visaoAtual = sensor.getVisaoIdentificacao();
		int pesoMinimo = Integer.MAX_VALUE;

		for (int i = 0; i < norte.length; i++) {
			if (visaoAtual[i] == poupador) {
				persegisoesPossíveis.add(cima);
			}
		}

		for (int i = 0; i < sul.length; i++) {
			if (visaoAtual[i] == poupador) {
				persegisoesPossíveis.add(baixo);
			}
		}

		for (int i = 0; i < oeste.length; i++) {
			if (visaoAtual[i] == poupador) {
				persegisoesPossíveis.add(esquerda);
			}
		}

		for (int i = 0; i < leste.length; i++) {
			if (visaoAtual[i] == poupador) {
				persegisoesPossíveis.add(esquerda);
			}
		}

		if (!persegisoesPossíveis.isEmpty()) {
			return persegisoesPossíveis.get(ThreadLocalRandom.current().nextInt(persegisoesPossíveis.size()));
		}

		if (visaoAtual[verCima] == caminho) {
			int pesoDaVisao = pesos.getPeso(posicaoAtual.x, posicaoAtual.y - 1);
			if (pesoDaVisao == pesoMinimo)
				acoesPossiveis.add(cima);
			else if (pesoDaVisao < pesoMinimo) {
				pesoMinimo = pesoDaVisao;
				acoesPossiveis = new ArrayList<>();
				acoesPossiveis.add(cima);
			}
		}

		if (visaoAtual[verBaixo] == caminho) {
			int pesoDaVisao = pesos.getPeso(posicaoAtual.x, posicaoAtual.y + 1);
			if (pesoDaVisao == pesoMinimo)
				acoesPossiveis.add(baixo);
			else if (pesoDaVisao <= pesoMinimo) {
				pesoMinimo = pesoDaVisao;
				acoesPossiveis = new ArrayList<>();
				acoesPossiveis.add(baixo);
			}
		}

		if (visaoAtual[verEsquerda] == caminho) {
			int pesoDaVisao = pesos.getPeso(posicaoAtual.x - 1, posicaoAtual.y);
			if (pesoDaVisao == pesoMinimo)
				acoesPossiveis.add(esquerda);
			else if (pesoDaVisao <= pesoMinimo) {
				pesoMinimo = pesoDaVisao;
				acoesPossiveis = new ArrayList<>();
				acoesPossiveis.add(esquerda);
			}
		}

		if (visaoAtual[verDireita] == caminho) {
			int pesoDaVisao = pesos.getPeso(posicaoAtual.x + 1, posicaoAtual.y);
			if (pesoDaVisao == pesoMinimo)
				acoesPossiveis.add(direita);
			else if (pesoDaVisao <= pesoMinimo) {
				pesoMinimo = pesoDaVisao;
				acoesPossiveis = new ArrayList<>();
				acoesPossiveis.add(direita);
			}
		}

		if (acoesPossiveis.isEmpty()) {
			return parado;
		}

		int escolhido = ThreadLocalRandom.current().nextInt(acoesPossiveis.size());
		return acoesPossiveis.get(escolhido);
	}

	public int reagirAoAmbiente() {
		return 0;
	}

	public int roubar() {

		int[] visao = sensor.getVisaoIdentificacao();
		if (visao[verCima] == poupador) {
			return cima;
		} else if (visao[verBaixo] == poupador) {
			return baixo;
		} else if (visao[verEsquerda] == poupador) {
			return esquerda;
		} else if (visao[verDireita] == poupador) {
			return direita;
		}

		pesos.aumetarPeso(sensor.getPosicao());
		return explorar();
		// return reagirAoAmbiente();

	}

	public int acao() {
		// pesos.aumetarPeso(sensor.getPosicao());
		return roubar();

	}

}
