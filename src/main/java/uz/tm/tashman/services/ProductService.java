package uz.tm.tashman.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.tm.tashman.entity.Product;
import uz.tm.tashman.entity.ProductCategory;
import uz.tm.tashman.entity.ProductImage;
import uz.tm.tashman.entity.User;
import uz.tm.tashman.enums.EProductCategory;
import uz.tm.tashman.enums.Language;
import uz.tm.tashman.enums.VolumeUnit;
import uz.tm.tashman.models.*;
import uz.tm.tashman.models.requestModels.ProductRequestModel;
import uz.tm.tashman.models.wrapperModels.ResPageable;
import uz.tm.tashman.repository.ProductCategoryRepository;
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
import static uz.tm.tashman.util.Util.getImageUrl;
import static uz.tm.tashman.util.Util.isBlank;

@Service
public class ProductService extends HTTPUtil {

    final UserService userService;
    final LogService logService;
    final ProductRepository productRepository;
    final ProductImageRepository productImageRepository;
    final ProductCategoryRepository productCategoryRepository;

    public ProductService(
            UserService userService,
            LogService logService,
            ProductRepository productRepository,
            ProductImageRepository productImageRepository,
            ProductCategoryRepository productCategoryRepository) {
        this.userService = userService;
        this.logService = logService;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    public ResponseModel<Product> createProduct(ProductRequestModel productModel, User user) {
        ResponseModel<Product> responseModel = new ResponseModel<>();

        try {
            Product product = new Product();

            Optional<ProductCategory> optProductCategory = productCategoryRepository.findByCode(productModel.getProductCategory());
            if (!optProductCategory.isPresent()) {
                responseModel.setSuccess(false);
                responseModel.setMessage("Product Category not found");
                return responseModel;
            }

            ProductCategory productCategory = optProductCategory.get();

            product.setProductCategory(productCategory);
            product.setSlug(productModel.getSlug());
            product.setMetaTitleEn(productModel.getMetaTitleEn());
            product.setMetaTitleRu(productModel.getMetaTitleRu());
            product.setMetaTitleUz(productModel.getMetaTitleUz());
            product.setMetaDescriptionEn(productModel.getMetaDescriptionEn());
            product.setMetaDescriptionRu(productModel.getMetaDescriptionRu());
            product.setMetaDescriptionUz(productModel.getMetaDescriptionUz());
            product.setNameEn(productModel.getNameEn());
            product.setNameRu(productModel.getNameRu());
            product.setNameUz(productModel.getNameUz());
            product.setShortDescriptionEn(productModel.getShortDescriptionEn());
            product.setShortDescriptionRu(productModel.getShortDescriptionRu());
            product.setShortDescriptionUz(productModel.getShortDescriptionUz());
            product.setFullDescriptionEn(productModel.getFullDescriptionEn());
            product.setFullDescriptionRu(productModel.getFullDescriptionRu());
            product.setFullDescriptionUz(productModel.getFullDescriptionUz());
            product.setPiecesPerPackage(productModel.getPiecesPerPackage());
            product.setPackageNettoWeight(productModel.getPackageNettoWeight());
            product.setPackageBruttoWeight(productModel.getPackageBruttoWeight());
            product.setPackageDimensions(productModel.getPackageDimensions());
            product.setVolume(productModel.getVolume());
            product.setExpireDurationUnit(productModel.getExpireDurationUnit());
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

    public ProductModel getProductModel(Product product, Language language) {
        ProductModel productModel = new ProductModel();

        productModel.setSlug(product.getSlug());
        productModel.setMetaTitle(product.getMetaTitleByLanguage(language));
        productModel.setMetaDescription(product.getMetaDescriptionByLanguage(language));
        productModel.setProductCategory(product.getProductCategory().getProductCategoryByLanguage(language));
        productModel.setName(product.getNameByLanguage(language));
        productModel.setShortDescription(product.getShortDescriptionByLanguage(language));
        productModel.setFullDescription(product.getFullDescriptionByLanguage(language));
        productModel.setPiecesPerPackage(product.getPiecesPerPackage());
        productModel.setPackageNettoWeight(product.getPackageNettoWeight());
        productModel.setPackageBruttoWeight(product.getPackageBruttoWeight());
        productModel.setPackageDimensions(product.getPackageDimensions());
        productModel.setVolume(product.getVolume());
        productModel.setExpireDuration(product.getExpireDuration());
        productModel.setExpireDurationUnit(product.getExpireDurationUnit().getNameByLanguage(language));
        productModel.setPrice(product.getPrice());
        productModel.setVolumeUnit(product.getVolumeUnit().getNameByLanguage(language));

        List<ProductImage> productImageList = productImageRepository.findAllByProduct(product);
        if (!isBlank(productImageList)) {
            List<ProductImageModel> imageModels = new ArrayList<>();

            productImageList.forEach(productImage -> imageModels.add(getProductImageModel(productImage)));

            productModel.setImages(imageModels);
        }


        /* these properties are not necessary in product model */
/*
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
*/

        return productModel;
    }

    public Product getProductBySlug(String slug) {
        if (slug == null) {
            return null;
        }

        Optional<Product> optProduct = productRepository.findBySlug(slug);

        return optProduct.orElse(null);
    }

    public ProductImageModel getProductImageModel(ProductImage productImage) {
        ProductImageModel productImageModel = new ProductImageModel();
        productImageModel.setId(productImage.getId());
        productImageModel.setSortOrder(productImage.getSortOrder());

        productImageModel.setImageUrl(getImageUrl(productImage.getImageUrl()));
        productImageModel.setThumbnailImageUrl(getImageUrl(productImage.getThumbnailImageUrl()));

        return productImageModel;
    }

    public ResponseEntity<?> addProduct(ProductRequestModel productModel, HttpServletRequest header) {
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
                return OkResponse(UNABLE_TO_ADD_PRODUCT, product.getMessage());
            }
        } catch (Exception e) {
            return InternalServerErrorResponse(e);
        }
    }

    public ResponseEntity<?> list(BasicModel basicModel, HttpServletRequest header) {
        try {
            Pageable pageable = Util.getPageable(basicModel.getPage(), basicModel.getPageSize());

            Page<Product> productPage = productRepository.findAll(pageable);

            List<ProductModel> productModelList = new ArrayList<>();

            productPage.getContent().forEach(product -> productModelList.add(getProductModel(product, basicModel.getLanguage())));

            ResPageable resPageable = new ResPageable(
                    productModelList,
                    productPage.getTotalElements(),
                    productPage.getTotalPages(),
                    basicModel.getPage(),
                    basicModel.getPageSize());

            return OkResponse(SUCCESSFULLY_FOUND, resPageable);
        } catch (Exception e) {
            return InternalServerErrorResponse(e);
        }
    }

    public ResponseEntity<?> getProductCategoryList(BasicModel basicModel) {
        try {
            User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<HashMapModel> productCategoryList = EProductCategory.getAllByLanguage(admin.getLanguage());

            return OkResponse(SUCCESS, productCategoryList);
        } catch (Exception e) {
            return InternalServerErrorResponse(e);
        }
    }

    public ResponseEntity<?> getVolumeUnitList(BasicModel basicModel) {
        try {
            User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<HashMapModel> volumeUnitList = VolumeUnit.getAllByLanguage(admin.getLanguage());

            return OkResponse(SUCCESS, volumeUnitList);
        } catch (Exception e) {
            return InternalServerErrorResponse(e);
        }
    }
}