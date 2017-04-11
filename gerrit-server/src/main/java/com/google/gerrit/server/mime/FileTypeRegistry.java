begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.mime
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mime
package|;
end_package

begin_import
import|import
name|eu
operator|.
name|medsea
operator|.
name|mimeutil
operator|.
name|MimeType
import|;
end_import

begin_import
import|import
name|eu
operator|.
name|medsea
operator|.
name|mimeutil
operator|.
name|MimeUtil2
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_interface
DECL|interface|FileTypeRegistry
specifier|public
interface|interface
name|FileTypeRegistry
block|{
comment|/**    * Get the most specific MIME type available for a file.    *    * @param path name of the file. The base name (component after the last '/') may be used to help    *     determine the MIME type, such as by examining the extension (portion after the last '.' if    *     present).    * @param content the complete file content. If non-null the content may be used to guess the MIME    *     type by examining the beginning for common file headers.    * @return the MIME type for this content. If the MIME type is not recognized or cannot be    *     determined, {@link MimeUtil2#UNKNOWN_MIME_TYPE} which is an alias for {@code    *     application/octet-stream}.    */
DECL|method|getMimeType (String path, byte[] content)
name|MimeType
name|getMimeType
parameter_list|(
name|String
name|path
parameter_list|,
name|byte
index|[]
name|content
parameter_list|)
function_decl|;
comment|/**    * Get the most specific MIME type available for a file.    *    * @param path name of the file. The base name (component after the last '/') may be used to help    *     determine the MIME type, such as by examining the extension (portion after the last '.' if    *     present).    * @param is InputStream corresponding to the complete file content. The content may be used to    *     guess the MIME type by examining the beginning for common file headers.    * @return the MIME type for this content. If the MIME type is not recognized or cannot be    *     determined, {@link MimeUtil2#UNKNOWN_MIME_TYPE} which is an alias for {@code    *     application/octet-stream}.    */
DECL|method|getMimeType (String path, InputStream is)
name|MimeType
name|getMimeType
parameter_list|(
name|String
name|path
parameter_list|,
name|InputStream
name|is
parameter_list|)
function_decl|;
comment|/**    * Is this content type safe to transmit to a browser directly?    *    * @param type the MIME type of the file content.    * @return true if the Gerrit administrator wants to permit this content to be served as-is; false    *     if the administrator does not trust this content type and wants it to be protected    *     (typically by wrapping the data in a ZIP archive).    */
DECL|method|isSafeInline (MimeType type)
name|boolean
name|isSafeInline
parameter_list|(
name|MimeType
name|type
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

