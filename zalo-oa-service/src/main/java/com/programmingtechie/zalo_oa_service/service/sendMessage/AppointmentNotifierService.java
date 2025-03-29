package com.programmingtechie.zalo_oa_service.service.sendMessage;

import java.util.HashMap;
import java.util.Map;

import com.programmingtechie.zalo_oa_service.service.token.TokenService;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.programmingtechie.zalo_oa_service.config.ZaloConfig;
import com.programmingtechie.zalo_oa_service.dto.request.appointmentNotifier.AppointmentCancelInfo;
import com.programmingtechie.zalo_oa_service.dto.request.appointmentNotifier.AppointmentInfo;
import com.programmingtechie.zalo_oa_service.enums.IconURLAddress;
import com.programmingtechie.zalo_oa_service.enums.NavigationURLAddress;
import com.programmingtechie.zalo_oa_service.enums.ZaloApiEndpoint;
import com.programmingtechie.zalo_oa_service.oa.APIException;
import com.programmingtechie.zalo_oa_service.oa.ZaloOaClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentNotifierService {
    final ZaloConfig zaloConfig;
    final TokenService tokenService;

    private JsonObject createJsonElement(String type, Map<String, String> properties) {
        JsonObject element = new JsonObject();
        element.addProperty("type", type);
        properties.forEach(element::addProperty);
        return element;
    }

    private JsonObject getContentWithKeyAndValue(String key, String value) {
        return createJsonElement("content", Map.of("key", key, "value", value));
    }

    private JsonObject getContentWithKeyAndValue(String key, String value, String style) {
        return createJsonElement("content", Map.of("key", key, "value", value, "style", style));
    }

    private JsonObject createButton(String type, String title, String imageIcon, JsonObject payload) {
        JsonObject button = new JsonObject();
        button.add("payload", payload);
        button.addProperty("image_icon", imageIcon);
        button.addProperty("title", title);
        button.addProperty("type", type);
        return button;
    }

    public Void notifyBookingSuccess(AppointmentInfo info) throws APIException {
        try {
            log.info("AppointmentInfo: " + info.toString());
            ZaloOaClient client = new ZaloOaClient();
            String access_token = tokenService.getTokenResponse().getAccessToken();

            log.info("access_token: " + access_token);
            Map<String, String> headers = new HashMap<>();
            headers.put("access_token", access_token);

            // các phần tử JSON sử dụng phương thức createJsonElement
            JsonObject element1 = createJsonElement("banner", Map.of("image_url", IconURLAddress.BANNER.getUrl()));
            JsonObject element2 = createJsonElement(
                    "header", Map.of("align", "center", "content", "\uD83D\uDCC4 LỊCH HẸN ĐÃ ĐẶT THÀNH CÔNG"));
            JsonObject element3 = createJsonElement(
                    "text",
                    Map.of(
                            "align",
                            "left",
                            "content",
                            "Xin chào! Lịch hẹn khám bệnh của bạn đã được đặt thành công! Thông tin như sau:"));

            JsonObject content1 = getContentWithKeyAndValue("Mã lịch hẹn", info.getAppointmentId());
            JsonObject content2 = getContentWithKeyAndValue("Họ tên", info.getName());
            JsonObject content3 = getContentWithKeyAndValue("Dịch vụ khám", info.getServiceName());
            JsonObject content4 = getContentWithKeyAndValue("Ngày giờ khám", info.getDate() + " - " + info.getTime());
            JsonObject content5 =
                    getContentWithKeyAndValue("Phòng khám", info.getRoomName() + " - " + info.getDoctorName());
            JsonObject content6 = getContentWithKeyAndValue("Trạng thái", info.getStatus(), "yellow");

            JsonArray content = new JsonArray();
            content.add(content1);
            content.add(content2);
            content.add(content3);
            content.add(content4);
            content.add(content5);
            content.add(content6);

            JsonObject element4 = new JsonObject();
            element4.addProperty("type", "table");
            element4.add("content", content);

            JsonObject element5 = createJsonElement(
                    "text",
                    Map.of(
                            "align",
                            "left",
                            "content",
                            "❗ Lưu ý: Lịch hẹn sẽ có hiệu lực sau khi được xác nhận từ phía phòng khám và sẽ thông tin đến bạn trong thời gian sớm nhất. Trân trọng!"));

            JsonArray elements = new JsonArray();
            elements.add(element1);
            elements.add(element2);
            elements.add(element3);
            elements.add(element4);
            elements.add(element5);

            // các button
            JsonObject payloadButton1 = new JsonObject();
            payloadButton1.addProperty("url", NavigationURLAddress.INFORMATION.getUrl());
            JsonObject button1 = createButton(
                    "oa.open.url", "Thông tin chi tiết", IconURLAddress.INFORMATION.getUrl(), payloadButton1);

            JsonObject payloadButton3 = new JsonObject();
            payloadButton3.addProperty("url", NavigationURLAddress.CANCEL_APPOINTMENT.getUrl());
            JsonObject button3 = createButton(
                    "oa.open.url", "Hủy lịch hẹn", IconURLAddress.DOCUMENT_CANCEL.getUrl(), payloadButton3);

            JsonObject payloadButton4 = new JsonObject();
            payloadButton4.addProperty("phone_code", "84123456789");
            JsonObject button4 = createButton(
                    "oa.open.phone", "Liên hệ hỗ trợ", IconURLAddress.CUSTOMER_SUPPORT.getUrl(), payloadButton4);

            JsonArray buttons = new JsonArray();
            buttons.add(button1);
            buttons.add(button3);
            buttons.add(button4);

            // payload và thông điệp
            JsonObject payload = new JsonObject();
            payload.add("elements", elements);
            payload.add("buttons", buttons);
            payload.addProperty("template_type", "transaction_booking");
            payload.addProperty("language", "VI");

            JsonObject attachment = new JsonObject();
            attachment.addProperty("type", "template");
            attachment.add("payload", payload);

            JsonObject message = new JsonObject();
            message.add("attachment", attachment);

            JsonObject recipient = new JsonObject();
            recipient.addProperty("user_id", info.getUserId());

            JsonObject body = new JsonObject();
            body.add("recipient", recipient);
            body.add("message", message);
            System.err.println(body.toString());

            // Gửi yêu cầu API
            JsonObject excuteRequest = client.excuteRequest(
                    ZaloApiEndpoint.SEND_MESSAGE_TRANSACTION.getUrl(), "POST", null, body, headers, null);
            log.info("API request result: " + excuteRequest);
            return null;
        } catch (APIException e) {
            log.error("Error sending appointment notification: {}", e.getMessage());
        }
        return null;
    }

    public Void confirmAppointment(AppointmentInfo info) throws APIException {
        try {
            log.info("AppointmentInfo: " + info.toString());
            ZaloOaClient client = new ZaloOaClient();
            String access_token = tokenService.getTokenResponse().getAccessToken();

            log.info("access_token: " + access_token);
            Map<String, String> headers = new HashMap<>();
            headers.put("access_token", access_token);

            // các phần tử JSON sử dụng phương thức createJsonElement
            JsonObject element1 = createJsonElement("banner", Map.of("image_url", IconURLAddress.BANNER.getUrl()));
            JsonObject element2 = createJsonElement(
                    "header", Map.of("align", "center", "content", "✅ LỊCH HẸN ĐÃ ĐƯỢC XÁC NHẬN"));
            JsonObject element3 = createJsonElement(
                    "text",
                    Map.of(
                            "align",
                            "left",
                            "content",
                            "Xin chào! Lịch hẹn khám bệnh của bạn đã được đặt thành công! Thông tin như sau:"));

            JsonObject content1 = getContentWithKeyAndValue("Mã lịch hẹn", info.getAppointmentId());
            JsonObject content2 = getContentWithKeyAndValue("Họ tên", info.getName());
            JsonObject content3 = getContentWithKeyAndValue("Dịch vụ khám", info.getServiceName());
            JsonObject content4 = getContentWithKeyAndValue("Ngày giờ khám", info.getDate() + " - " + info.getTime());
            JsonObject content5 = getContentWithKeyAndValue("Số thứ tự", info.getOrderNumber());
            JsonObject content6 =
                    getContentWithKeyAndValue("Phòng khám", info.getRoomName() + " - " + info.getDoctorName());
            JsonObject content7 = getContentWithKeyAndValue("Trạng thái", info.getStatus(), "green");

            JsonArray content = new JsonArray();
            content.add(content1);
            content.add(content2);
            content.add(content3);
            content.add(content4);
            content.add(content5);
            content.add(content6);
            content.add(content7);

            JsonObject element4 = new JsonObject();
            element4.addProperty("type", "table");
            element4.add("content", content);

            JsonObject element5 = createJsonElement(
                    "text",
                    Map.of(
                            "align",
                            "left",
                            "content",
                            "Để buổi khám diễn ra thuận lợi, vui lòng đến đúng giờ và mang theo thẻ căn cước, thẻ BHYT (nếu có) cùng các giấy tờ liên quan khác. Rất mong được đón tiếp bạn!"));

            JsonArray elements = new JsonArray();
            elements.add(element1);
            elements.add(element2);
            elements.add(element3);
            elements.add(element4);
            elements.add(element5);

            // các button
            JsonObject payloadButton1 = new JsonObject();
            payloadButton1.addProperty("url", NavigationURLAddress.INFORMATION.getUrl());
            JsonObject button1 = createButton(
                    "oa.open.url", "Thông tin chi tiết", IconURLAddress.INFORMATION.getUrl(), payloadButton1);

            JsonObject payloadButton3 = new JsonObject();
            payloadButton3.addProperty("url", NavigationURLAddress.CANCEL_APPOINTMENT.getUrl());
            JsonObject button3 = createButton(
                    "oa.open.url", "Hủy lịch hẹn", IconURLAddress.DOCUMENT_CANCEL.getUrl(), payloadButton3);

            JsonObject payloadButton4 = new JsonObject();
            payloadButton4.addProperty("phone_code", "84123456789");
            JsonObject button4 = createButton(
                    "oa.open.phone", "Liên hệ hỗ trợ", IconURLAddress.CUSTOMER_SUPPORT.getUrl(), payloadButton4);

            JsonArray buttons = new JsonArray();
            buttons.add(button1);
            buttons.add(button3);
            buttons.add(button4);

            // payload và thông điệp
            JsonObject payload = new JsonObject();
            payload.add("elements", elements);
            payload.add("buttons", buttons);
            payload.addProperty("template_type", "transaction_booking");
            payload.addProperty("language", "VI");

            JsonObject attachment = new JsonObject();
            attachment.addProperty("type", "template");
            attachment.add("payload", payload);

            JsonObject message = new JsonObject();
            message.add("attachment", attachment);

            JsonObject recipient = new JsonObject();
            recipient.addProperty("user_id", info.getUserId());

            JsonObject body = new JsonObject();
            body.add("recipient", recipient);
            body.add("message", message);
            System.err.println(body.toString());

            // Gửi yêu cầu API
            JsonObject excuteRequest = client.excuteRequest(
                    ZaloApiEndpoint.SEND_MESSAGE_TRANSACTION.getUrl(), "POST", null, body, headers, null);
            log.info("API request result: " + excuteRequest);
            return null;
        } catch (APIException e) {
            log.error("Error sending appointment notification: {}", e.getMessage());
        }
        return null;
    }

    public Void cancelAppointment(AppointmentCancelInfo appointmentCancelInfo) throws APIException {
        try {
            AppointmentInfo info = appointmentCancelInfo.getAppointmentInfo();
            log.info("AppointmentInfo: " + info.toString());
            ZaloOaClient client = new ZaloOaClient();
            String access_token = tokenService.getTokenResponse().getAccessToken();

            log.info("access_token: " + access_token);
            Map<String, String> headers = new HashMap<>();
            headers.put("access_token", access_token);

            // các phần tử JSON sử dụng phương thức createJsonElement
            JsonObject element1 = createJsonElement("banner", Map.of("image_url", IconURLAddress.BANNER.getUrl()));
            JsonObject element2 =
                    createJsonElement("header", Map.of("align", "center", "content", "❌ LỊCH HẸN ĐÃ ĐƯỢC HỦY"));
            JsonObject element3 = createJsonElement(
                    "text",
                    Map.of(
                            "align",
                            "left",
                            "content",
                            "Xin chào! Chúng tôi rất tiếc khi phải thông báo rằng lịch hẹn khám của bạn đã bị hủy. Thông tin chi tiết như sau:"));

            JsonObject content1 = getContentWithKeyAndValue("Mã lịch hẹn", info.getAppointmentId());
            JsonObject content2 = getContentWithKeyAndValue("Họ tên", info.getName());
            JsonObject content3 = getContentWithKeyAndValue("Dịch vụ khám", info.getServiceName());
            JsonObject content4 = getContentWithKeyAndValue("Ngày giờ khám", info.getDate() + " - " + info.getTime());
            JsonObject content5 =
                    getContentWithKeyAndValue("Phòng khám", info.getRoomName() + " - " + info.getDoctorName());
            JsonObject content6 = getContentWithKeyAndValue("Trạng thái", info.getStatus(), "red");
            JsonObject content7 = getContentWithKeyAndValue("Lý do", appointmentCancelInfo.getReason());

            JsonArray content = new JsonArray();
            content.add(content1);
            content.add(content2);
            content.add(content3);
            content.add(content4);
            content.add(content5);
            content.add(content6);
            content.add(content7);

            JsonObject element4 = new JsonObject();
            element4.addProperty("type", "table");
            element4.add("content", content);

            JsonObject element5 = createJsonElement(
                    "text",
                    Map.of(
                            "align",
                            "left",
                            "content",
                            "Nếu có bất kỳ nhầm lẫn nào hoặc bạn vẫn muốn tiếp tục khám bệnh, đừng lo lắng! Bạn có thể dễ dàng đặt lại lịch hẹn hoặc liên hệ với phòng khám, chúng tôi luôn sẵn sàng hỗ trợ bạn."));

            JsonArray elements = new JsonArray();
            elements.add(element1);
            elements.add(element2);
            elements.add(element3);
            elements.add(element4);
            elements.add(element5);

            // các button
            JsonObject payloadButton1 = new JsonObject();
            payloadButton1.addProperty("url", NavigationURLAddress.INFORMATION.getUrl());
            JsonObject button1 = createButton(
                    "oa.open.url", "Thông tin chi tiết", IconURLAddress.INFORMATION.getUrl(), payloadButton1);

            JsonObject payloadButton3 = new JsonObject();
            payloadButton3.addProperty("url", NavigationURLAddress.RESCHEDULE_APPOINTMENT.getUrl());
            JsonObject button3 = createButton(
                    "oa.open.url",
                    "Đặt lịch khám mới",
                    IconURLAddress.RESCHEDULE_APPOINTMENT.getUrl(),
                    payloadButton3);

            JsonObject payloadButton2 = new JsonObject();
            payloadButton2.addProperty("phone_code", "84123456789");
            JsonObject button2 = createButton(
                    "oa.open.phone", "Liên hệ hỗ trợ", IconURLAddress.CUSTOMER_SUPPORT.getUrl(), payloadButton2);

            JsonArray buttons = new JsonArray();
            buttons.add(button1);
            buttons.add(button3);
            buttons.add(button2);

            // payload và thông điệp
            JsonObject payload = new JsonObject();
            payload.add("elements", elements);
            payload.add("buttons", buttons);
            payload.addProperty("template_type", "transaction_booking");
            payload.addProperty("language", "VI");

            JsonObject attachment = new JsonObject();
            attachment.addProperty("type", "template");
            attachment.add("payload", payload);

            JsonObject message = new JsonObject();
            message.add("attachment", attachment);

            JsonObject recipient = new JsonObject();
            recipient.addProperty("user_id", info.getUserId());

            JsonObject body = new JsonObject();
            body.add("recipient", recipient);
            body.add("message", message);
            System.err.println(body.toString());

            // Gửi yêu cầu API
            JsonObject excuteRequest = client.excuteRequest(
                    ZaloApiEndpoint.SEND_MESSAGE_TRANSACTION.getUrl(), "POST", null, body, headers, null);
            log.info("API request result: " + excuteRequest);
            return null;
        } catch (APIException e) {
            log.error("Error sending appointment notification: {}", e.getMessage());
        }
        return null;
    }

    public Void remindAppointment(AppointmentInfo info) throws APIException {
        try {
            log.info("AppointmentInfo: " + info.toString());
            ZaloOaClient client = new ZaloOaClient();
            String access_token = tokenService.getTokenResponse().getAccessToken();

            log.info("access_token: " + access_token);
            Map<String, String> headers = new HashMap<>();
            headers.put("access_token", access_token);

            // các phần tử JSON sử dụng phương thức createJsonElement
            JsonObject element1 = createJsonElement("banner", Map.of("image_url", IconURLAddress.BANNER.getUrl()));
            JsonObject element2 = createJsonElement(
                    "header", Map.of("align", "center", "content", "\uD83E\uDE7A NHẮC LỊCH HẸN KHÁM NGÀY MAI"));
            JsonObject element3 = createJsonElement(
                    "text",
                    Map.of(
                            "align",
                            "left",
                            "content",
                            "Xin chào! Ngày mai bạn có lịch hẹn khám! Đừng quên sắp xếp thời gian để đến đúng giờ nhé. Thông tin như sau:"));

            JsonObject content1 = getContentWithKeyAndValue("Mã lịch hẹn", info.getAppointmentId());
            JsonObject content2 = getContentWithKeyAndValue("Họ tên", info.getName());
            JsonObject content3 = getContentWithKeyAndValue("Dịch vụ khám", info.getServiceName());
            JsonObject content4 = getContentWithKeyAndValue("Ngày giờ khám", info.getDate() + " - " + info.getTime());
            JsonObject content5 = getContentWithKeyAndValue("Số thứ tự", info.getOrderNumber());
            JsonObject content6 =
                    getContentWithKeyAndValue("Phòng khám", info.getRoomName() + " - " + info.getDoctorName());
            JsonObject content7 = getContentWithKeyAndValue("Trạng thái", info.getStatus(), "grey");

            JsonArray content = new JsonArray();
            content.add(content1);
            content.add(content2);
            content.add(content3);
            content.add(content4);
            content.add(content5);
            content.add(content6);
            content.add(content7);

            JsonObject element4 = new JsonObject();
            element4.addProperty("type", "table");
            element4.add("content", content);

            JsonObject element5 = createJsonElement(
                    "text",
                    Map.of(
                            "align",
                            "left",
                            "content",
                            "Để buổi khám diễn ra thuận lợi, vui lòng đến đúng giờ và mang theo thẻ căn cước, thẻ BHYT (nếu có) cùng các giấy tờ liên quan khác. Rất mong được đón tiếp bạn!"));

            JsonArray elements = new JsonArray();
            elements.add(element1);
            elements.add(element2);
            elements.add(element3);
            elements.add(element4);
            elements.add(element5);

            // các button
            JsonObject payloadButton1 = new JsonObject();
            payloadButton1.addProperty("url", NavigationURLAddress.INFORMATION.getUrl());
            JsonObject button1 = createButton(
                    "oa.open.url", "Thông tin chi tiết", IconURLAddress.INFORMATION.getUrl(), payloadButton1);

            JsonObject payloadButton3 = new JsonObject();
            payloadButton3.addProperty("phone_code", "84123456789");
            JsonObject button3 = createButton(
                    "oa.open.phone", "Liên hệ hỗ trợ", IconURLAddress.CUSTOMER_SUPPORT.getUrl(), payloadButton3);

            JsonArray buttons = new JsonArray();
            buttons.add(button1);
            buttons.add(button3);

            // payload và thông điệp
            JsonObject payload = new JsonObject();
            payload.add("elements", elements);
            payload.add("buttons", buttons);
            payload.addProperty("template_type", "transaction_booking");
            payload.addProperty("language", "VI");

            JsonObject attachment = new JsonObject();
            attachment.addProperty("type", "template");
            attachment.add("payload", payload);

            JsonObject message = new JsonObject();
            message.add("attachment", attachment);

            JsonObject recipient = new JsonObject();
            recipient.addProperty("user_id", info.getUserId());

            JsonObject body = new JsonObject();
            body.add("recipient", recipient);
            body.add("message", message);
            System.err.println(body.toString());

            // Gửi yêu cầu API
            JsonObject excuteRequest = client.excuteRequest(
                    ZaloApiEndpoint.SEND_MESSAGE_TRANSACTION.getUrl(), "POST", null, body, headers, null);
            log.info("API request result: " + excuteRequest);
            return null;
        } catch (APIException e) {
            log.error("Error sending appointment notification: {}", e.getMessage());
        }
        return null;
    }

    public Void sendThankYouMessage(AppointmentInfo info) throws APIException {
        try {
            log.info("AppointmentInfo: " + info.toString());
            ZaloOaClient client = new ZaloOaClient();
            String access_token = tokenService.getTokenResponse().getAccessToken();

            log.info("access_token: " + access_token);
            Map<String, String> headers = new HashMap<>();
            headers.put("access_token", access_token);

            // các phần tử JSON sử dụng phương thức createJsonElement
            JsonObject element1 = createJsonElement("banner", Map.of("image_url", IconURLAddress.BANNER.getUrl()));
            JsonObject element2 = createJsonElement(
                    "header", Map.of("align", "center", "content", "\uD83D\uDCE9 CẢM ƠN BẠN ĐÃ ĐẾN KHÁM!"));
            JsonObject element3 = createJsonElement(
                    "text",
                    Map.of(
                            "align",
                            "left",
                            "content",
                            "Xin chào! Cảm ơn bạn đã tin tưởng và đến khám bệnh tại phòng khám! Chúng tôi hy vọng bạn đã có trải nghiệm tốt trong buổi khám này."));

            JsonObject content1 = getContentWithKeyAndValue("Mã lịch hẹn", info.getAppointmentId());
            JsonObject content2 = getContentWithKeyAndValue("Họ tên", info.getName());
            JsonObject content3 = getContentWithKeyAndValue("Dịch vụ khám", info.getServiceName());
            JsonObject content4 = getContentWithKeyAndValue("Ngày giờ khám", info.getDate() + " - " + info.getTime());
            JsonObject content6 =
                    getContentWithKeyAndValue("Phòng khám", info.getRoomName() + " - " + info.getDoctorName());
            JsonObject content7 = getContentWithKeyAndValue("Trạng thái", info.getStatus(), "blue");

            JsonArray content = new JsonArray();
            content.add(content1);
            content.add(content2);
            content.add(content3);
            content.add(content4);
            content.add(content6);
            content.add(content7);

            JsonObject element4 = new JsonObject();
            element4.addProperty("type", "table");
            element4.add("content", content);

            JsonObject element5 = createJsonElement(
                    "text",
                    Map.of(
                            "align",
                            "left",
                            "content",
                            "\uD83E\uDE7A Nhắc nhở: Hãy lưu ý dùng thuốc theo đúng hướng dẫn của bác sĩ và đừng quên đặt lịch tái khám nếu cần theo dõi thêm. Chúc bạn luôn khỏe mạnh! Nếu cần hỗ trợ thêm, đừng ngần ngại liên hệ với chúng tôi nhé!"));

            JsonArray elements = new JsonArray();
            elements.add(element1);
            elements.add(element2);
            elements.add(element3);
            elements.add(element4);
            elements.add(element5);

            // các button
            JsonObject payloadButton1 = new JsonObject();
            payloadButton1.addProperty("url", NavigationURLAddress.INFORMATION.getUrl());
            JsonObject button1 = createButton(
                    "oa.open.url", "Thông tin chi tiết", IconURLAddress.INFORMATION.getUrl(), payloadButton1);

            JsonObject payloadButton2 = new JsonObject();
            payloadButton2.addProperty("url", NavigationURLAddress.APPOINTMENT_PAYMENT.getUrl());
            JsonObject button2 = createButton(
                    "oa.open.url", "Đánh giá dịch vụ", IconURLAddress.REVIEW_APPOINTMENT.getUrl(), payloadButton2);

            JsonObject payloadButton3 = new JsonObject();
            payloadButton3.addProperty("url", NavigationURLAddress.RESCHEDULE_APPOINTMENT.getUrl());
            JsonObject button3 = createButton(
                    "oa.open.url",
                    "Đặt lịch tái khám",
                    IconURLAddress.RESCHEDULE_APPOINTMENT.getUrl(),
                    payloadButton3);

            JsonObject payloadButton4 = new JsonObject();
            payloadButton4.addProperty("phone_code", "84123456789");
            JsonObject button4 = createButton(
                    "oa.open.phone", "Liên hệ hỗ trợ", IconURLAddress.CUSTOMER_SUPPORT.getUrl(), payloadButton4);

            JsonArray buttons = new JsonArray();
            buttons.add(button1);
            buttons.add(button2);
            buttons.add(button3);

            // payload và thông điệp
            JsonObject payload = new JsonObject();
            payload.add("elements", elements);
            payload.add("buttons", buttons);
            payload.addProperty("template_type", "transaction_booking");
            payload.addProperty("language", "VI");

            JsonObject attachment = new JsonObject();
            attachment.addProperty("type", "template");
            attachment.add("payload", payload);

            JsonObject message = new JsonObject();
            message.add("attachment", attachment);

            JsonObject recipient = new JsonObject();
            recipient.addProperty("user_id", info.getUserId());

            JsonObject body = new JsonObject();
            body.add("recipient", recipient);
            body.add("message", message);
            System.err.println(body.toString());

            // Gửi yêu cầu API
            JsonObject excuteRequest = client.excuteRequest(
                    ZaloApiEndpoint.SEND_MESSAGE_TRANSACTION.getUrl(), "POST", null, body, headers, null);
            log.info("API request result: " + excuteRequest);
            return null;
        } catch (APIException e) {
            log.error("Error sending appointment notification: {}", e.getMessage());
        }
        return null;
    }
}
