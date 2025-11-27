import Model.Cliente;
import Model.Endereco;
import Model.Entrega;
import Model.Produto;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Endereco endereco = new Endereco(
                12L,
                "PE",
                "Recife",
                "Boa Viagem",
                "Av. Boa Viagem",
                "51011-000",
                123
        );

        Cliente cliente = new Cliente(

                "C_SP_1025",
                "Danilo Mendes",
                "111222333-50",
                "08/12/2003",
                "danilo.gw@gmail.com",
                "senha123"
        );

        Entrega entrega = new Entrega(
                "XJTU2025",
                "10/10/2025",
                "05/11/2025",
                "Amazon",
                new BigDecimal("35.50")
        );

        Produto produto = new Produto(
                145200,
                "Teclado Rizen",
                "Teclado mecanico, 75% com RGB",
                new BigDecimal("150"),
                10
        );

        System.out.println("Cliente: " + cliente);
        System.out.println("Endereço: " + endereco);
        System.out.println("Entrega: " + entrega);
        System.out.println("Produto: " + produto);

    }
}
