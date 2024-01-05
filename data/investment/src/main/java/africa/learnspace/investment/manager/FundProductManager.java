package africa.learnspace.investment.manager;

import africa.learnspace.investment.model.FundProduct;
import africa.learnspace.investment.repository.FundProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import java.util.Optional;

@Component
public class FundProductManager {

    private final FundProductRepository fundProductRepository;

    public FundProductManager(FundProductRepository fundProductRepository) {
        this.fundProductRepository = fundProductRepository;
    }

    public Optional<FundProduct> findFundProductByName(String name) {
        return fundProductRepository.findByName(name);
    }

    public void saveFundProduct(FundProduct fundProduct) {
        fundProductRepository.save(fundProduct);
    }

    public Page<FundProduct> viewAllFundProduct(PageRequest pageRequest) {
        return fundProductRepository.findAll(pageRequest);
    }


    public FundProduct findByFundProductById(String fundProductId){
        return fundProductRepository.findByFundProductId(fundProductId);

    }
}
