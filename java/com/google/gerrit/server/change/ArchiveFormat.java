begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2013 Google Inc. All Rights Reserved.
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
comment|//     http://www.apache.org/licenses/LICENSE-2.0
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|compress
operator|.
name|archivers
operator|.
name|ArchiveOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|api
operator|.
name|ArchiveCommand
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|api
operator|.
name|ArchiveCommand
operator|.
name|Format
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|archive
operator|.
name|TarFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|archive
operator|.
name|Tbz2Format
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|archive
operator|.
name|TgzFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|archive
operator|.
name|TxzFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|archive
operator|.
name|ZipFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|FileMode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectLoader
import|;
end_import

begin_enum
DECL|enum|ArchiveFormat
specifier|public
enum|enum
name|ArchiveFormat
block|{
DECL|enumConstant|TGZ
name|TGZ
argument_list|(
literal|"application/x-gzip"
argument_list|,
operator|new
name|TgzFormat
argument_list|()
argument_list|)
block|,
DECL|enumConstant|TAR
name|TAR
argument_list|(
literal|"application/x-tar"
argument_list|,
operator|new
name|TarFormat
argument_list|()
argument_list|)
block|,
DECL|enumConstant|TBZ2
name|TBZ2
argument_list|(
literal|"application/x-bzip2"
argument_list|,
operator|new
name|Tbz2Format
argument_list|()
argument_list|)
block|,
DECL|enumConstant|TXZ
name|TXZ
argument_list|(
literal|"application/x-xz"
argument_list|,
operator|new
name|TxzFormat
argument_list|()
argument_list|)
block|,
DECL|enumConstant|ZIP
name|ZIP
argument_list|(
literal|"application/x-zip"
argument_list|,
operator|new
name|ZipFormat
argument_list|()
argument_list|)
block|;
DECL|field|format
specifier|private
specifier|final
name|ArchiveCommand
operator|.
name|Format
argument_list|<
name|?
argument_list|>
name|format
decl_stmt|;
DECL|field|mimeType
specifier|private
specifier|final
name|String
name|mimeType
decl_stmt|;
DECL|method|ArchiveFormat (String mimeType, ArchiveCommand.Format<?> format)
name|ArchiveFormat
parameter_list|(
name|String
name|mimeType
parameter_list|,
name|ArchiveCommand
operator|.
name|Format
argument_list|<
name|?
argument_list|>
name|format
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
name|this
operator|.
name|mimeType
operator|=
name|mimeType
expr_stmt|;
name|ArchiveCommand
operator|.
name|registerFormat
argument_list|(
name|name
argument_list|()
argument_list|,
name|format
argument_list|)
expr_stmt|;
block|}
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
DECL|method|getMimeType ()
name|String
name|getMimeType
parameter_list|()
block|{
return|return
name|mimeType
return|;
block|}
DECL|method|getDefaultSuffix ()
name|String
name|getDefaultSuffix
parameter_list|()
block|{
return|return
name|getSuffixes
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
DECL|method|getSuffixes ()
name|Iterable
argument_list|<
name|String
argument_list|>
name|getSuffixes
parameter_list|()
block|{
return|return
name|format
operator|.
name|suffixes
argument_list|()
return|;
block|}
DECL|method|createArchiveOutputStream (OutputStream o)
specifier|public
name|ArchiveOutputStream
name|createArchiveOutputStream
parameter_list|(
name|OutputStream
name|o
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|(
name|ArchiveOutputStream
operator|)
name|this
operator|.
name|format
operator|.
name|createArchiveOutputStream
argument_list|(
name|o
argument_list|)
return|;
block|}
DECL|method|putEntry (T out, String path, byte[] data)
specifier|public
parameter_list|<
name|T
extends|extends
name|Closeable
parameter_list|>
name|void
name|putEntry
parameter_list|(
name|T
name|out
parameter_list|,
name|String
name|path
parameter_list|,
name|byte
index|[]
name|data
parameter_list|)
throws|throws
name|IOException
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|ArchiveCommand
operator|.
name|Format
argument_list|<
name|T
argument_list|>
name|fmt
init|=
operator|(
name|Format
argument_list|<
name|T
argument_list|>
operator|)
name|format
decl_stmt|;
name|fmt
operator|.
name|putEntry
argument_list|(
name|out
argument_list|,
literal|null
argument_list|,
name|path
argument_list|,
name|FileMode
operator|.
name|REGULAR_FILE
argument_list|,
operator|new
name|ObjectLoader
operator|.
name|SmallObject
argument_list|(
name|FileMode
operator|.
name|TYPE_FILE
argument_list|,
name|data
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_enum

end_unit
