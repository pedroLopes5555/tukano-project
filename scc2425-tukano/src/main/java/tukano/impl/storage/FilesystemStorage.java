package tukano.impl.storage;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Consumer;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

import tukano.api.Result;
import static tukano.api.Result.ErrorCode.BAD_REQUEST;
import static tukano.api.Result.ErrorCode.INTERNAL_ERROR;
import static tukano.api.Result.ErrorCode.NOT_FOUND;
import static tukano.api.Result.error;
import static tukano.api.Result.ok;
import utils.IO;

public class FilesystemStorage implements BlobStorage {
	private final String rootDir;
	private static final int CHUNK_SIZE = 4096;
	private static final String DEFAULT_ROOT_DIR = "/tmp/";

	private static BlobContainerClient _blobClient;
	private static final String storageConnectionString = "####";
	private static final String BLOBS_CONTAINER_NAME = "blobs";

	public FilesystemStorage() {
		this.rootDir = DEFAULT_ROOT_DIR;
		_blobClient = new BlobContainerClientBuilder()
							.connectionString(storageConnectionString)
							.containerName(BLOBS_CONTAINER_NAME)
							.buildClient();
	}
	
	@Override
	public Result<Void> write(String path, byte[] bytes) {

		BlobClient blob = _blobClient.getBlobClient(path);
		blob.upload(BinaryData.fromBytes(bytes));

		return ok();
	}

	@Override
	public Result<byte[]> read(String path) {
			
		BlobClient blob = _blobClient.getBlobClient(path);
		BinaryData data = blob.downloadContent();
		byte[] bytes = data.toBytes();

		return bytes != null ? ok( bytes ) : error( INTERNAL_ERROR );
	}

	@Override
	public Result<Void> read(String path, Consumer<byte[]> sink) {
		if (path == null)
			return error(BAD_REQUEST);
		
		var file = toFile( path );
		if( ! file.exists() )
			return error(NOT_FOUND);
		
		IO.read( file, CHUNK_SIZE, sink );
		return ok();
	}
	
	@Override
	public Result<Void> delete(String path) {
		if (path == null)
			return error(BAD_REQUEST);

		try {
			var file = toFile( path );
			Files.walk(file.toPath())
			.sorted(Comparator.reverseOrder())
			.map(Path::toFile)
			.forEach(File::delete);
		} catch (IOException e) {
			e.printStackTrace();
			return error(INTERNAL_ERROR);
		}
		return ok();
	}
	
	private File toFile(String path) {
		var res = new File( rootDir + path );
		
		var parent = res.getParentFile();
		if( ! parent.exists() )
			parent.mkdirs();
		
		return res;
	}

	
}
