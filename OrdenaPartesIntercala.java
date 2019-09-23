package Trabalho02;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;

public class OrdenaPartesIntercala {

	public static void main(String[] args) throws IOException {

		ArrayList<Endereco> a = new ArrayList<Endereco>();
		RandomAccessFile f = new RandomAccessFile("cep.dat", "r");

		int qtdArq = 8;
		long tamanhoRegistro = 300L; // tamanho em bytes de um registro
		long qtdRegistro = f.length() / tamanhoRegistro;
		long qtdRegPorArq = qtdRegistro / qtdArq;
		long resto = qtdRegistro % qtdArq; // pegar o resto de qtdRegPorArq e colocar no ultimo arquivo

		// dividir o arquivo em 8 partes
		// pegar cada pedaço de arquivo, ler e add no ArrayList
		// depois ordenar e escrever em um novo arquivo

		for (int i = 1; i <= qtdArq; i++) {

			if (i == qtdArq) { // ultimo arquivo
				for (int j = 0; j < qtdRegPorArq + resto; j++) {
					Endereco e = new Endereco();
					e.leEndereco(f);
					a.add(e);
				}

			} else {
				for (int j = 0; j < qtdRegPorArq; j++) {
					Endereco e = new Endereco();
					e.leEndereco(f);
					a.add(e);
				}
			}

			Collections.sort(a, new ComparaCEP());
			RandomAccessFile f2 = new RandomAccessFile("cep_ordenado_" + i + ".dat", "rw");
			for (Endereco e1 : a) {
				e1.escreveEndereco(f2);
			}
			f2.close();
			a.clear();
		}
		f.close();

		// intercalar de 2 em 2 arquivos ate ter o arquivo completo
		intercala();
	}

	private static void intercala() {

		int numArq = 8;
		int numArqTotal = (numArq * 2) - 1;
		int j = numArq;

		ArrayList<Endereco> a = new ArrayList<Endereco>();

		try {
			for (int i = 1; i <= numArqTotal; i++) {

				// pegar indice impar e que nao seja o ultimo
				if (i % 2 != 0 && i != numArqTotal) {

					RandomAccessFile f1 = new RandomAccessFile("cep_ordenado_" + (i) + ".dat", "r");
					RandomAccessFile f2 = new RandomAccessFile("cep_ordenado_" + (i + 1) + ".dat", "r");

					ComparaCEP comp = new ComparaCEP();

					Endereco e1 = new Endereco();
					e1.leEndereco(f1);
					Endereco e2 = new Endereco();
					e2.leEndereco(f2);

					boolean finalArq1 = false;
					boolean finalArq2 = false;

					// enquanto nenhum dos 2 acabou
					while (!(finalArq1) && !(finalArq2)) {
						if (comp.compare(e1, e2) < 0) {
							try {
								a.add(e1);
								e1 = new Endereco();
								e1.leEndereco(f1);
							} catch (EOFException e) {
								finalArq1 = true;
							}
						} else {
							try {
								a.add(e2);
								e2 = new Endereco();
								e2.leEndereco(f2);
							} catch (EOFException e) {
								finalArq2 = true;
							}
						}
					}

					while (!(finalArq1)) {
						try {
							a.add(e1);
							e1 = new Endereco();
							e1.leEndereco(f1);
						} catch (EOFException e) {
							finalArq1 = true;
						}
					}

					while (!(finalArq2)) {
						try {
							a.add(e2);
							e2 = new Endereco();
							e2.leEndereco(f2);
						} catch (EOFException e) {
							finalArq2 = true;
						}
					}

					RandomAccessFile saida = new RandomAccessFile("cep_ordenado_" + (i + j) + ".dat", "rw");
					for (Endereco e : a) {
						e.escreveEndereco(saida);
					}
					saida.close();
					a.clear();
					j--;

					f1.close();
					File file1 = new File("cep_ordenado_" + (i) + ".dat");
					file1.delete();
					f2.close();
					File file2 = new File("cep_ordenado_" + (i + 1) + ".dat");
					file2.delete();
					File fileFinal = new File("cep_ordenado_15.dat");
					fileFinal.renameTo(new File("cep_ordenado_completo.dat"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
