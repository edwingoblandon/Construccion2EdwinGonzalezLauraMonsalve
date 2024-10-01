package app.dao.interfaces;

import app.dto.ProductDto;
import java.util.List;


public interface ProductDao {
    
    public ProductDto findById(ProductDto producDto) throws Exception;
    public List<ProductDto> findAllProducts() throws Exception;
}
