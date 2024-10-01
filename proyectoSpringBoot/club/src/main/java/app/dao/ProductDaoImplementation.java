package app.dao;

import app.dao.interfaces.ProductDao;
import app.dao.repository.ProductRepository;
import app.dto.ProductDto;
import app.helpers.Helper;
import app.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Setter
@Getter
@NoArgsConstructor
@Service
public class ProductDaoImplementation implements ProductDao {
    
    @Autowired
    ProductRepository productRepository;
    
    @Override
    public ProductDto findById(ProductDto productDto) throws Exception{
        Optional<Product> optionalProduct = productRepository.findById(productDto.getId());
        
        if(!optionalProduct.isPresent()) throw new Exception("El producto no se encontro");
        
        Product product = optionalProduct.get();
        
        return Helper.parse(product);
    }
    
    @Override
    public List<ProductDto> findAllProducts() throws Exception{
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) throw new Exception("No se encontraron productos disponibles");

        return products.stream().map(Helper::parse).collect(Collectors.toList());
    }
}
