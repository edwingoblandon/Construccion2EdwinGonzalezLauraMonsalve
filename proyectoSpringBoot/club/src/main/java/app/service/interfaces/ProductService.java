package app.service.interfaces;

import app.dto.ProductDto;
import java.util.List;

public interface ProductService {
    
    public List<ProductDto> getAllProducts() throws Exception;
    
}
