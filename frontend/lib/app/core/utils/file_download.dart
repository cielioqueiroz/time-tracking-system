import 'dart:convert';
import 'dart:js_interop';
import 'dart:typed_data';

import 'package:web/web.dart' as web;

/// Triggers a browser download of [content] as a file named [filename].
///
/// Web-only on purpose — the app targets Flutter web. Builds an in-memory blob
/// and clicks a transient anchor, so no server round-trip beyond the data we
/// already hold.
void downloadTextFile(String content, String filename, {String mime = 'text/csv'}) {
  final Uint8List bytes = Uint8List.fromList(utf8.encode(content));
  final blob = web.Blob(
    <JSAny>[bytes.toJS].toJS,
    web.BlobPropertyBag(type: '$mime;charset=utf-8'),
  );
  final url = web.URL.createObjectURL(blob);
  final anchor = web.document.createElement('a') as web.HTMLAnchorElement
    ..href = url
    ..download = filename;
  anchor.click();
  web.URL.revokeObjectURL(url);
}
