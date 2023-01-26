package uz.tm.tashman.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.tm.tashman.entity.Product;
import uz.tm.tashman.entity.ProductImage;
import uz.tm.tashman.entity.User;
import uz.tm.tashman.models.BasicModel;
import uz.tm.tashman.models.ProductModel;
import uz.tm.tashman.models.ResponseModel;
import uz.tm.tashman.models.wrapperModels.ResPageable;
import uz.tm.tashman.repository.ProductImageRepository;
import uz.tm.tashman.repository.ProductRepository;
import uz.tm.tashman.util.HTTPUtil;
import uz.tm.tashman.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static uz.tm.tashman.enums.StatusCodes.*;
import static uz.tm.tashman.util.CONSTANT.*;

@Service
public class ProductService extends HTTPUtil {

    final UserService userService;
    final LogService logService;
    final ProductRepository productRepository;
    final ProductImageRepository productImageRepository;

    public ProductService(
            UserService userService,
            LogService logService,
            ProductRepository productRepository,
            ProductImageRepository productImageRepository) {
        this.userService = userService;
        this.logService = logService;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    public ResponseModel<Product> createProduct(ProductModel productModel, User user) {
        ResponseModel<Product> responseModel = new ResponseModel<>();

        try {
            Product product = new Product();

            product.setSlug(productModel.getSlug());
            product.setMetaTitle(productModel.getMetaTitle());
            product.setMetaDescription(productModel.getMetaDescription());
            product.setProductCategory(productModel.getProductCategory());
            product.setName(productModel.getName());
            product.setShortDescription(productModel.getShortDescription());
            product.setFullDescription(productModel.getFullDescription());
            product.setPiecesPerPackage(productModel.getPiecesPerPackage());
            product.setPackageNettoWeight(productModel.getPackageNettoWeight());
            product.setPackageBruttoWeight(productModel.getPackageBruttoWeight());
            product.setPackageDimensions(productModel.getPackageDimensions());
            product.setVolume(productModel.getVolume());
            product.setExpireDuration(productModel.getExpireDuration());
            product.setPrice(productModel.getPrice());
            product.setVolumeUnit(productModel.getVolumeUnit());

            product.setIsActive(false);
            product.setCreatedDate(LocalDateTime.now());
            product.setCreatedBy(user.getId());
            product.setIsDeleted(false);

            product = productRepository.save(product);

            List<ProductImage> productImages = new ArrayList<>();
            int index = 0;
            for (MultipartFile image : productModel.getImages()) {
                String uploadDirectory = DATA_FOLDER + PRODUCTS_FOLDER + "/" + product.getSlug();
                String fileName = Integer.toString(index);
                String fileExtension = ".png";
                ResponseModel<String> imageSaver = Util.saveImage(uploadDirectory, fileName, fileExtension, image);

                if (imageSaver.getSuccess()) {
                    ProductImage productImage = new ProductImage();
                    productImage.setProduct(product);
                    productImage.setImageUrl(imageSaver.getData());
                    productImage.setSortOrder(index);
                    productImage.setCreatedBy(user.getId());
                    productImage.setCreatedDate(LocalDateTime.now());
                    productImage.setIsDeleted(false);

                    String thumbnailUploadDirectory = DATA_FOLDER + THUMBNAILS_FOLDER + "/" + product.getSlug();
                    Integer thumbnailSize = 500;
                    ResponseModel<String> thumbnailCreator = Util.createThumbnail(thumbnailUploadDirectory, fileName, fileExtension, image, thumbnailSize);

                    if (thumbnailCreator.getSuccess()) {
                        productImage.setThumbnailImageUrl(thumbnailCreator.getData());
                    } else {
                        if (thumbnailCreator.getException() == null) {
                            logService.saveToLog(thumbnailCreator.getMessage());
                        } else {
                            logService.saveToLog(thumbnailCreator.getException());
                        }
                    }

                    productImage = productImageRepository.save(productImage);
                    productImages.add(productImage);
                } else {
                    logService.saveToLog(imageSaver.getException());
                }
                index++;
            }
            product.setImage(productImages);
            product = productRepository.save(product);

            responseModel.setSuccess(true);
            responseModel.setData(product);
        } catch (Exception e) {
            logService.saveToLog(exceptionAsString(e));

            responseModel.setSuccess(false);
            responseModel.setException(e);
        }

        return responseModel;
    }

    public ProductModel getProductModel(Product product) {
        ProductModel productModel = new ProductModel();

        productModel.setSlug(product.getSlug());
        productModel.setMetaTitle(product.getMetaTitle());
        productModel.setMetaDescription(product.getMetaDescription());
        productModel.setProductCategory(product.getProductCategory());
        productModel.setName(product.getName());
        productModel.setShortDescription(product.getShortDescription());
        productModel.setFullDescription(product.getFullDescription());
        productModel.setPiecesPerPackage(product.getPiecesPerPackage());
        productModel.setPackageNettoWeight(product.getPackageNettoWeight());
        productModel.setPackageBruttoWeight(product.getPackageBruttoWeight());
        productModel.setPackageDimensions(product.getPackageDimensions());
        productModel.setVolume(product.getVolume());
        productModel.setExpireDuration(product.getExpireDuration());
        productModel.setPrice(product.getPrice());
        productModel.setVolumeUnit(product.getVolumeUnit());
//        product.setImage(productModel.getImageUrls());

        productModel.setIsActive(product.getIsActive());
        productModel.setCreatedDate(product.getCreatedDate());
        productModel.setIsDeleted(product.getIsDeleted());

        if (product.getDeletedBy() != null) {
            User admin = userService.getUserById(product.getDeletedBy());
            String adminName = "Admin profile not found";
            if (admin != null) {
                adminName = admin.getAdmin().getFullName();
            }
            productModel.setDeletedBy(adminName);
        }

        if (product.getCreatedBy() != null) {
            User admin = userService.getUserById(product.getCreatedBy());
            String adminName = "Admin profile not found";
            if (admin != null) {
                adminName = admin.getAdmin().getFullName();
            }
            productModel.setCreatedBy(adminName);
        }

        return productModel;
    }

    public Product getProductBySlug(String slug) {
        if (slug == null) {
            return null;
        }

        Optional<Product> optProduct = productRepository.findBySlug(slug);

        return optProduct.orElse(null);
    }

    public ResponseEntity<?> addProduct(ProductModel productModel, HttpServletRequest header) {
        try {
            User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Product existingProduct = getProductBySlug(productModel.getSlug());

            if (existingProduct != null) {
                return OkResponse(PRODUCT_ALREADY_REGISTERED);
            }

            ResponseModel<Product> product = createProduct(productModel, admin);
            if (product.getSuccess()) {
                return OkResponse(SUCCESSFULLY_ADDED);
            } else {
                return OkResponse(UNABLE_TO_ADD_PRODUCT);
            }
        } catch (Exception e) {
            logService.saveToLog(exceptionAsString(e));
            return InternalServerErrorResponse(exceptionAsString(e));
        }
    }

    public ResponseEntity<?> list(BasicModel basicModel, HttpServletRequest header) {
        try {
            Pageable pageable = Util.getPageable(basicModel.getPage(), basicModel.getPageSize());

            Page<Product> productPage = productRepository.findAll(pageable);

            List<ProductModel> productModelList = new ArrayList<>();

            productPage.getContent().forEach(product -> productModelList.add(getProductModel(product)));

            ResPageable resPageable = new ResPageable(
                    productModelList,
                    productPage.getTotalElements(),
                    productPage.getTotalPages(),
                    basicModel.getPage(),
                    basicModel.getPageSize());

            return OkResponse(SUCCESSFULLY_FOUND, resPageable);
        } catch (Exception e) {
            logService.saveToLog(exceptionAsString(e));
            return InternalServerErrorResponse(exceptionAsString(e));
        }
    }
}