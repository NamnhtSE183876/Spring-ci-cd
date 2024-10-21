package swp.koi.service.auctionTypeService;

import swp.koi.dto.request.AuctionTypeDTO;
import swp.koi.model.AuctionType;

public interface AuctionTypeService {
    AuctionType findByAuctionTypeName(String auctionTypeDTO);

    boolean existById(Integer auctionTypeId);

    void saveType(AuctionType auctionType);
}