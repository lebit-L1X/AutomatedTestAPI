package endpoints;

import model.APIResponse;

import java.util.Map;

public class Warranty extends BaseTestMethods {
    private static final String CUSTOMER_ENDPOINT = "/warranty/customer/create";
    public APIResponse createWarranty(Map<String, Object> customerCreate) {
        return postMultipart(
                CUSTOMER_ENDPOINT,
                customerCreate,
                getToken(),
                "brand",
                "model",
                "plate_number",
                "odometer",
                "store_id",
                "purchase_date",
                "invoice_number",
                "tires[0][tire_type]",
                "tires[0][tire_size]",
                "tires[0][barcode]",
                "tires[0][barcode_image]",
                "tires[0][tire_number]",
                "tires[0][tire_number_image]",
                "tires[1][tire_type]",
                "tires[1][tire_size]",
                "tires[1][barcode]",
                "tires[1][barcode_image]",
                "tires[1][tire_number]",
                "tires[1][tire_number_image]",
                "invoice",
                "image_odometer"
        );
    }


}
