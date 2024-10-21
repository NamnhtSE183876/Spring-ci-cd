package swp.koi.service.auctionService;

import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Scheduled;
import swp.koi.dto.request.AuctionWithLotsDTO;
import swp.koi.dto.request.UpdateStatusDTO;
import swp.koi.dto.response.AuctionResponseDTO;
import swp.koi.dto.response.LotResponseDto;
import swp.koi.model.Auction;
import swp.koi.model.Lot;

import java.util.List;

public interface AuctionService {
    @Scheduled(fixedDelay = 1000 * 60)
    void updateAuctionStatusAndEndTime();

    AuctionResponseDTO createAuctionWithLots(@Valid AuctionWithLotsDTO request);

    LotResponseDto getLot(Integer lotId);

    List<Auction> getAllAuction();

    Auction getAuction(Integer auctionId);

    List<Auction> getAllOnGoingAuction();

    List<Auction> getAllCompletedAuction();
}