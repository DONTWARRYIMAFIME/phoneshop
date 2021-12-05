$(function () {
    $("div.error").children("span:contains('NumberFormatException')").each(function() {
        $(this).text(() => "Invalid format");
    });
});