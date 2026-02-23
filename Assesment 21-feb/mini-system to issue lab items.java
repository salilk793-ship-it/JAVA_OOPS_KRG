import java.util.*;

class Student {
    String studentid;
    String studentname;
    int pendingfine;
    int borroweditems;

    public Student(String studentid, String studentname, int pendingfine, int borroweditems) {
        this.studentid = studentid;
        this.studentname = studentname;
        this.pendingfine = pendingfine;
        this.borroweditems = borroweditems;
    }

    public void checkeligibility() {
        if (pendingfine > 0) {
            throw new IllegalStateException("Student has pending fine.");
        }
        if (borroweditems >= 2) {
            throw new IllegalStateException("Borrow limit reached.");
        }
    }

    public void increaseborrowcount() {
        borroweditems++;
    }
}

class Asset {
    String id;
    String name;
    boolean isavailable;
    int securitylevel;

    public Asset(String id, String name, boolean isavailable, int securitylevel) {
        this.id = id;
        this.name = name;
        this.isavailable = isavailable;
        this.securitylevel = securitylevel;
    }

    public void checkaccess(String studentid) {
        if (!isavailable) {
            throw new IllegalStateException("Asset not available.");
        }
        if (securitylevel == 3 && !studentid.startsWith("KRG")) {
            throw new SecurityException("Restricted asset. UID not authorized.");
        }
    }
}

class CheckoutRequest {
    String studentid;
    String assetid;
    int requestedhours;

    public CheckoutRequest(String studentid, String assetid, int requestedhours) {
        this.studentid = studentid;
        this.assetid = assetid;
        this.requestedhours = requestedhours;
    }
}

class ValidationUtil {

    public static void validatestudentid(String studentid) {
        if (studentid == null || studentid.length() < 8 || studentid.length() > 12 || studentid.contains(" ")) {
            throw new IllegalArgumentException("Invalid UID format.");
        }
    }

    public static void validateassetid(String assetid) {
        if (assetid == null || !assetid.matches("LAB-\\d+")) {
            throw new IllegalArgumentException("Invalid Asset ID format.");
        }
    }

    public static void validatehours(int hours) {
        if (hours < 1 || hours > 6) {
            throw new IllegalArgumentException("Hours must be between 1 and 6.");
        }
    }
}

class AssetStore {
    Asset[] assets;

    public AssetStore(Asset[] assets) {
        this.assets = assets;
    }

    public Asset getasset(String assetid) {
        for (Asset asset : assets) {
            if (asset.id.equals(assetid)) {
                return asset;
            }
        }
        throw new NullPointerException("Asset not found: " + assetid);
    }

    public void borrowasset(Asset asset) {
        if (!asset.isavailable) {
            throw new IllegalStateException("Asset already borrowed.");
        }
        asset.isavailable = false;
    }
}

class AuditLogger {
    public static void log(String message) {
    }

    public static void logerror(Exception e) {
    }
}

class CheckoutService {

    Student[] studentrecords;
    AssetStore assetinventory;

    public CheckoutService(Student[] studentrecords, AssetStore assetinventory) {
        this.studentrecords = studentrecords;
        this.assetinventory = assetinventory;
    }

    public Student findstudent(String studentid) {
        for (Student student : studentrecords) {
            if (student.studentid.equals(studentid)) {
                return student;
            }
        }
        throw new NullPointerException("Student not found: " + studentid);
    }

    public String processcheckout(CheckoutRequest request)
            throws IllegalArgumentException, IllegalStateException,
            SecurityException, NullPointerException {

        ValidationUtil.validatestudentid(request.studentid);
        ValidationUtil.validateassetid(request.assetid);
        ValidationUtil.validatehours(request.requestedhours);

        Student student = findstudent(request.studentid);
        Asset asset = assetinventory.getasset(request.assetid);

        student.checkeligibility();
        asset.checkaccess(request.studentid);

        int finalhours = request.requestedhours;

        if (asset.name.contains("Cable") && finalhours > 3) {
            finalhours = 3;
        }

        assetinventory.borrowasset(asset);
        student.increaseborrowcount();

        return "TXN-20260221-" + asset.id + "-" + student.studentid;
    }
}

public class Main {

    public static void main(String[] args) {

        Student[] studentrecords = {
                new Student("KRG711A", "Krish", 0, 0),
                new Student("KRG601A", "saiyam", 50, 0),
                new Student("KRG711B", "janish", 0, 2)
        };

        Asset[] assets = {
                new Asset("LAB-101", "HDMI Cable", true, 1),
                new Asset("LAB-102", "Raspberry Pi Kit", true, 3),
                new Asset("LAB-103", "Ethernet Cable", false, 1)
        };

        AssetStore assetinventory = new AssetStore(assets);
        CheckoutService service = new CheckoutService(studentrecords, assetinventory);

        CheckoutRequest[] requests = {
                new CheckoutRequest("KRG711A", "LAB-101", 5),
                new CheckoutRequest("KRG711A", "LAB-XYZ", 2),
                new CheckoutRequest("KRG601A", "LAB-102", 4)
        };

        for (CheckoutRequest request : requests) {
            try {
                service.processcheckout(request);
            }
            catch (IllegalArgumentException e) {
                AuditLogger.logerror(e);
            }
            catch (NullPointerException e) {
                AuditLogger.logerror(e);
            }
            catch (SecurityException e) {
                AuditLogger.logerror(e);
            }
            catch (IllegalStateException e) {
                AuditLogger.logerror(e);
            }
            finally {
                AuditLogger.log("Attempt finished for UID=" + request.studentid + ", asset=" + request.assetid);
            }
        }
    }
}
