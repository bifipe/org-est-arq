import java.io.RandomAccessFile;
import java.util.Scanner;

public class BuscaBinaria {
	
	public static void main(String[] args) throws Exception 
    {		
		// abre arquivo
        RandomAccessFile f = new RandomAccessFile("C:\\Users\\bia_p\\eclipse-workspace\\OEA\\src\\cep_ordenado.dat", "r");
    
        long inicio = 0;
        long tamanhoRegistro = 300L; // tamanho em bytes de um registro
        // long fim = f.length(); // retorna o tamanho do arquivo em bytes
        long fim = f.length()/tamanhoRegistro; // retorna a qtd de registro (numero do ultimo registro)
        boolean teste = false;
       
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Digite o CEP: ");
        String cep = scanner.nextLine();
       
        int i = 0;
        while(!teste) {
        	i++;
        	long meio = (inicio+fim)/2;
        	f.seek(meio*300); // ponteiro vai pro meio do arquivo
        	
        	Endereco e = new Endereco();
        	e.leEndereco(f); // le o endereco do meio
        	
        	if(Long.parseLong(e.getCep()) == Long.parseLong(cep)) {
        		System.out.println(e.getLogradouro());
        		teste = true;
        	}
        	else if(Long.parseLong(e.getCep()) > Long.parseLong(cep)) {
        		fim = meio - 1;	
        	}
        	else {
        		inicio = meio + 1;
        	}
        	if(fim < inicio) {
            	System.out.println("CEP nao encontrado!!");
            	break;
            }
        }
        
        System.out.println("Numero de comparaçoes: " + i);
        f.close();
    }

}
