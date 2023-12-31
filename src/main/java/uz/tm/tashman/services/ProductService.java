package uz.tm.tashman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.tm.tashman.entity.*;
import uz.tm.tashman.enums.EProductCategory;
import uz.tm.tashman.enums.Language;
import uz.tm.tashman.enums.VolumeUnit;
import uz.tm.tashman.models.*;
import uz.tm.tashman.models.requestModels.ProductRequestModel;
import uz.tm.tashman.models.wrapperModels.ResPageable;
import uz.tm.tashman.repository.ProductCategoryRepository;
import uz.tm.tashman.repository.ProductImageRepository;
import uz.tm.tashman.repository.ProductPackingRepository;
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

    final
    ProductPackingRepository productPackingRepository;

    final UserService userService;
    final LogService logService;
    final ProductRepository productRepository;
    final ProductImageRepository productImageRepository;
    final ProductCategoryRepository productCategoryRepository;
    final ProductMetaService productMetaService;


    public ProductService(
            UserService userService,
            LogService logService,
            ProductRepository productRepository,
            ProductImageRepository productImageRepository,
            ProductCategoryRepository productCategoryRepository, ProductMetaService productMetaService, ProductPackingRepository productPackingRepository) {
        this.userService = userService;
        this.logService = logService;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productMetaService = productMetaService;
        this.productPackingRepository = productPackingRepository;
    }

    public ResponseModel<Product> createProduct(ProductRequestModel productRequestModel, User user) {
        ResponseModel<Product> responseModel = new ResponseModel<>();

        try {
            Product product = new Product();

            Optional<ProductCategory> optProductCategory = productCategoryRepository.findByCodeAndIsDeletedFalse(productRequestModel.getProductCategory());
            if (!optProductCategory.isPresent()) {
                responseModel.setSuccess(false);
                responseModel.setMessage("Product Category not found");
                return responseModel;
            }

            ProductCategory productCategory = optProductCategory.get();

            product.setCategory(productCategory);
            product.setSlug(productRequestModel.getSlug());
            product.setNameEn(productRequestModel.getNameEn());
            product.setNameRu(productRequestModel.getNameRu());
            product.setNameUz(productRequestModel.getNameUz());
            product.setShortDescriptionEn(productRequestModel.getShortDescriptionEn());
            product.setShortDescriptionRu(productRequestModel.getShortDescriptionRu());
            product.setShortDescriptionUz(productRequestModel.getShortDescriptionUz());
            product.setFullDescriptionEn(productRequestModel.getFullDescriptionEn());
            product.setFullDescriptionRu(productRequestModel.getFullDescriptionRu());
            product.setFullDescriptionUz(productRequestModel.getFullDescriptionUz());

//            product.getProductPacking().setPiecesPerPackage(productModel.getPiecesPerPackage());
//            product.getProductPacking().setPackageNettoWeight(productModel.getPackageNettoWeight());
//            product.getProductPacking().setPackageBruttoWeight(productModel.getPackageBruttoWeight());
//            product.getProductPacking().setPackageDimensions(productModel.getPackageDimensions());
//            product.getProductPacking().setVolume(productModel.getVolume());
//            product.getProductPacking().setVolumeUnit(productModel.getVolumeUnit());

            product.setExpireDurationUnit(productRequestModel.getExpireDurationUnit());
            product.setExpireDuration(productRequestModel.getExpireDuration());
            product.setPrice(productRequestModel.getPrice());
            product.setInProduction(productRequestModel.getInProduction());
            product.setBarCode(productRequestModel.getBarCode());
            product.setStoreTemperature(productRequestModel.getStoreTemperature());
            product.setFirstLaunchedDate(productRequestModel.getFirstLaunchedDate());
            product.setBrand(productRequestModel.getBrand());
            product.setIsActive(false);
            product.setCreatedDate(LocalDateTime.now());
            product.setCreatedBy(user.getId());
            product.setIsDeleted(false);

            product = productRepository.save(product);

            ProductPacking productPacking = new ProductPacking();
            productPacking.setVolume(productRequestModel.getProductPackingModel().getVolume());
            productPacking.setPiecesPerPackage(productRequestModel.getProductPackingModel().getPiecesPerPackage());
            productPacking.setPackageNettoWeight(productRequestModel.getProductPackingModel().getPackageNettoWeight());
            productPacking.setPackageBruttoWeight(productRequestModel.getProductPackingModel().getPackageBruttoWeight());
            productPacking.setPackageDimensions(productRequestModel.getProductPackingModel().getPackageDimensions());
            productPacking.setVolumeUnit(VolumeUnit.getByCode(productRequestModel.getProductPackingModel().getVolumeUnit()));
            productPacking.setBoxQuantity(productRequestModel.getProductPackingModel().getBoxQuantity());

            productPacking = productPackingRepository.save(productPacking);
//            productPacking.setProduct(product);

            product.setProductPacking(productPacking);

            List<Assortment> assortmentList = new ArrayList<>();

            for (String assortmentName : productRequestModel.getAssortments()) {
                Assortment assortment = new Assortment();
                assortment.setProduct(product);
                assortment.setName(assortmentName);
                assortmentList.add(assortment);
            }
            product.setAssortments(assortmentList);
            product = productRepository.save(product);


//            ProductMeta productMeta = new ProductMeta();
//            productMeta.setProductId(product.getId());
//            productMeta.setMetaTitleEn(productModel.getMetas().getMetaTitleEn());
//            productMeta.setMetaTitleRu(productModel.getMetas().getMetaTitleRu());
//            productMeta.setMetaTitleUz(productModel.getMetas().getMetaTitleUz());
//            productMeta.setMetaDescriptionEn(productModel.getMetas().getMetaDescriptionEn());
//            productMeta.setMetaDescriptionRu(productModel.getMetas().getMetaDescriptionRu());
//            productMeta.setMetaDescriptionUz(productModel.getMetas().getMetaDescriptionUz());
//            productMetaService.save(productMeta);

            List<ProductImage> productImages = new ArrayList<>();
            int index = 0;
            for (MultipartFile image : productRequestModel.getImages()) {
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

    public ResponseModel<Product> updateProduct(ProductRequestModel productRequestModel, Product product) {
        ResponseModel<Product> responseModel = new ResponseModel<>();

        try {
            Optional<ProductCategory> optProductCategory = productCategoryRepository.findByCodeAndIsDeletedFalse(productRequestModel.getProductCategory());
            if (!optProductCategory.isPresent()) {
                responseModel.setSuccess(false);
                responseModel.setMessage("Product Category not found");
                return responseModel;
            }

            ProductCategory productCategory = optProductCategory.get();

            product.setCategory(productCategory);
            product.setSlug(productRequestModel.getSlug() == null ? product.getSlug() : productRequestModel.getSlug());
            product.setNameEn(productRequestModel.getNameEn() == null ? product.getNameEn() : productRequestModel.getNameEn());
            product.setNameRu(productRequestModel.getNameRu() == null ? product.getNameRu() : productRequestModel.getNameRu());
            product.setNameUz(productRequestModel.getNameUz() == null ? product.getNameUz() : productRequestModel.getNameUz());
            product.setShortDescriptionEn(productRequestModel.getShortDescriptionEn() == null ? product.getFullDescriptionEn() : productRequestModel.getShortDescriptionEn());
            product.setShortDescriptionRu(productRequestModel.getShortDescriptionRu() == null ? product.getFullDescriptionRu() : productRequestModel.getShortDescriptionRu());
            product.setShortDescriptionUz(productRequestModel.getShortDescriptionUz() == null ? product.getFullDescriptionUz() : productRequestModel.getShortDescriptionUz());
            product.setFullDescriptionEn(productRequestModel.getFullDescriptionEn() == null ? product.getFullDescriptionEn() : productRequestModel.getFullDescriptionEn());
            product.setFullDescriptionRu(productRequestModel.getFullDescriptionRu() == null ? product.getFullDescriptionRu() : productRequestModel.getFullDescriptionRu());
            product.setFullDescriptionUz(productRequestModel.getFullDescriptionUz() == null ? product.getFullDescriptionUz() : productRequestModel.getFullDescriptionUz());

//            ProductPacking productPacking = new ProductPacking();
//            productPacking.setPiecesPerPackage(productRequestModel.getPiecesPerPackage() == null ? product.getProductPacking().getPiecesPerPackage() : productRequestModel.getPiecesPerPackage());
//            productPacking.setPackageNettoWeight(productRequestModel.getPackageNettoWeight() == null ? product.getProductPacking().getPackageNettoWeight() : productRequestModel.getPackageNettoWeight());
//            productPacking.setPackageBruttoWeight(productRequestModel.getPackageBruttoWeight() == null ? product.getProductPacking().getPackageBruttoWeight() : productRequestModel.getPackageBruttoWeight());
//            productPacking.setPackageDimensions(productRequestModel.getPackageDimensions() == null ? product.getProductPacking().getPackageDimensions() : productRequestModel.getPackageDimensions());
//            productPacking.setVolume(productRequestModel.getVolume() == null ? product.getProductPacking().getVolume() : productRequestModel.getVolume());
//            productPacking.setVolumeUnit(productRequestModel.getVolumeUnit() == null ? product.getProductPacking().getVolumeUnit() : productRequestModel.getVolumeUnit());
            //productRepository.save(product);

            product.setExpireDurationUnit(productRequestModel.getExpireDurationUnit() == null ? product.getExpireDurationUnit() : productRequestModel.getExpireDurationUnit());
            product.setExpireDuration(productRequestModel.getExpireDuration() == null ? product.getExpireDuration() : productRequestModel.getExpireDuration());
            product.setPrice(productRequestModel.getPrice() == null ? product.getPrice() : productRequestModel.getPrice());
            product.setInProduction(productRequestModel.getInProduction() == null ? product.getInProduction() : productRequestModel.getInProduction());
            product.setBarCode(productRequestModel.getBarCode() == null ? product.getBarCode() : productRequestModel.getBarCode());
            product.setStoreTemperature(productRequestModel.getStoreTemperature() == null ? product.getStoreTemperature() : productRequestModel.getStoreTemperature());
            product.setFirstLaunchedDate(productRequestModel.getFirstLaunchedDate() == null ? product.getFirstLaunchedDate() : productRequestModel.getFirstLaunchedDate());
            product.setBrand(productRequestModel.getBrand() == null ? product.getBrand() : productRequestModel.getBrand());


            List<Assortment> listOfAssortments = new ArrayList<>();

            if (productRequestModel.getAssortments() != null) {
                for (String assortmentName : productRequestModel.getAssortments()) {
                    Assortment assortment = new Assortment();
                    assortment.setProduct(product);
                    assortment.setName(assortmentName);
                    listOfAssortments.add(assortment);
                }
                product.setAssortments(listOfAssortments);
            }

            productRepository.save(product);
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
        productModel.setCategory(product.getCategory().getProductCategoryByLanguage(language));
        productModel.setName(product.getNameByLanguage(language));
        productModel.setShortDescription(product.getShortDescriptionByLanguage(language));
        productModel.setFullDescription(product.getFullDescriptionByLanguage(language));

        ProductPackingModel productPackingModel = new ProductPackingModel();
        productPackingModel.setBoxQuantity(product.getProductPacking().getBoxQuantity());
        productPackingModel.setPiecesPerPackage(product.getProductPacking().getPiecesPerPackage());
        productPackingModel.setPackageDimensions(product.getProductPacking().getPackageDimensions());
        productPackingModel.setPackageNettoWeight(product.getProductPacking().getPackageNettoWeight());
        productPackingModel.setPackageBruttoWeight(product.getProductPacking().getPackageBruttoWeight());
        productPackingModel.setVolume(product.getProductPacking().getVolume());
        productPackingModel.setVolumeUnit(product.getProductPacking().getVolumeUnit().toString());

        productModel.setProductPackingModel(productPackingModel);

        productModel.setExpireDuration(product.getExpireDuration());
        productModel.setExpireDurationUnit(product.getExpireDurationUnit().getNameByLanguage(language));
        productModel.setPrice(product.getPrice());
        productModel.setInProduction(product.getInProduction());
        productModel.setBarCode(product.getBarCode());
        productModel.setStoreTemperature(product.getStoreTemperature());
        productModel.setFirstLaunchedDate(product.getFirstLaunchedDate());
        productModel.setBrand(product.getBrand());

        List<AssortmentResponseModel> assortmentResponseList = new ArrayList<>();

        for (Assortment assortment : product.getAssortments()) {
            AssortmentResponseModel assortmentResponseModel = new AssortmentResponseModel();
            assortmentResponseModel.setId(assortment.getId());
            assortmentResponseModel.setName(assortment.getName());
            assortmentResponseList.add(assortmentResponseModel);
        }
        productModel.setAssortments(assortmentResponseList);

        List<ProductImage> productImageList = productImageRepository.findAllByProduct(product);
        if (!isBlank(productImageList)) {
            List<ProductImageModel> imageModels = new ArrayList<>();

            productImageList.forEach(productImage -> imageModels.add(getProductImageModel(productImage)));

            productModel.setImages(imageModels);
        }

        Optional<ProductMeta> optionalProductMeta = productMetaService.findById(product.getId());
        if (optionalProductMeta.isPresent()) {
            ProductMeta productMeta = optionalProductMeta.get();
            productModel.setMetaTitle(productMeta.getMetaTitleByLanguage(language));
            productModel.setMetaDescription(productMeta.getMetaDescriptionByLanguage(language));
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

    public ResponseEntity<?> editProduct(ProductRequestModel productRequestModel, HttpServletRequest header) {
        try {
            User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Product> optionalProduct = productRepository.findById(productRequestModel.getId());
            if (!optionalProduct.isPresent()) {
                return BadRequestResponse(PRODUCT_NOT_FOUND);
            }
            Product product = optionalProduct.get();

            ResponseModel<Product> responseProductModel = updateProduct(productRequestModel, product);
            if (responseProductModel.getSuccess()) {
                return OkResponse(SUCCESSFULLY_EDITED);
            } else {
                return OkResponse(UNABLE_TO_EDIT_PRODUCT, responseProductModel.getMessage());
            }
        } catch (Exception e) {
            return InternalServerErrorResponse(e);
        }
    }

    public ResponseEntity<?> list(BasicModel basicModel, HttpServletRequest header) {

        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (isBlank(basicModel.getLanguage())) {
                basicModel.setLanguage(user.getLanguage());
            }
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

    public void setMetaData(ProductMetaModel metaData) {

    }


}