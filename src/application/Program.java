package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== TESTE 1: seller findById ===");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);

		System.out.println("\n=== TESTE 2: seller findByDepartment ===");

		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		for (Seller obj : list) {
			System.out.println(obj);
		}

		System.out.println("\n=== TESTE 3: seller findAll ===");
		list = sellerDao.findAll();
		for (Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("\n=== TESTE 4: seller insert ===");
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserido! Novo id = " + newSeller.getId());
		
		System.out.println("\n=== TESTE 5: seller update ===");
		seller = sellerDao.findById(1);//carrego os dados do vendedo com id 1 no objeto seller
		seller.setName("Martha Waine");// a partir do objeto seller, seto um novo nome para ele
		sellerDao.update(seller);//atualizo os dados do vendedor
		System.out.println("Update completado");
		
		System.out.println("\n=== TESTE 6: seller delete ===");
		System.out.print("Entre com o id para deletar: ");
		int id = sc.nextInt();
		sellerDao.deleteById(id);
		System.out.println("Delete completado!");
		
		sc.close();
	}

}
