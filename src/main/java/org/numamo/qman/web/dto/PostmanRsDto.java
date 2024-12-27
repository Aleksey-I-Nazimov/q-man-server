package org.numamo.qman.web.dto;


public final class PostmanRsDto {

    private boolean ok;
    private String description;

    public static PostmanRsDto ok (
            final String description
    ) {
        final PostmanRsDto response = new PostmanRsDto();
        response.setDescription(description);
        response.setOk(true);
        return response;
    }

    public static <T extends Throwable> PostmanRsDto error (
            final T anyException
    ) {
        final PostmanRsDto response = new PostmanRsDto();
        response.setDescription(anyException.getClass().getCanonicalName()+": "+anyException.getMessage());
        response.setOk(false);
        return response;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PostmanRsDto{" +
                "ok=" + ok +
                ", description='" + description + '\'' +
                '}';
    }
}
