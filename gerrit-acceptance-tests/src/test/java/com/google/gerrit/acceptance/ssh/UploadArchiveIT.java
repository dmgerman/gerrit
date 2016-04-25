begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|ssh
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Splitter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Iterables
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|AbstractDaemonTest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|GerritConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|NoHttpd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|PushOneCommit
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
name|zip
operator|.
name|ZipArchiveEntry
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
name|zip
operator|.
name|ZipArchiveInputStream
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
name|transport
operator|.
name|PacketLineIn
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
name|transport
operator|.
name|PacketLineOut
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
name|util
operator|.
name|IO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_class
annotation|@
name|NoHttpd
DECL|class|UploadArchiveIT
specifier|public
class|class
name|UploadArchiveIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"download.archive"
argument_list|,
name|value
operator|=
literal|"off"
argument_list|)
DECL|method|archiveFeatureOff ()
specifier|public
name|void
name|archiveFeatureOff
parameter_list|()
throws|throws
name|Exception
block|{
name|archiveNotPermitted
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"download.archive"
argument_list|,
name|values
operator|=
block|{
literal|"tar"
block|,
literal|"tbz2"
block|,
literal|"tgz"
block|,
literal|"txz"
block|}
argument_list|)
DECL|method|zipFormatDisabled ()
specifier|public
name|void
name|zipFormatDisabled
parameter_list|()
throws|throws
name|Exception
block|{
name|archiveNotPermitted
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|zipFormat ()
specifier|public
name|void
name|zipFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|String
name|abbreviated
init|=
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|abbreviate
argument_list|(
literal|8
argument_list|)
operator|.
name|name
argument_list|()
decl_stmt|;
name|String
name|c
init|=
name|command
argument_list|(
name|r
argument_list|,
name|abbreviated
argument_list|)
decl_stmt|;
name|InputStream
name|out
init|=
name|adminSshSession
operator|.
name|exec2
argument_list|(
literal|"git-upload-archive "
operator|+
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|argumentsToInputStream
argument_list|(
name|c
argument_list|)
argument_list|)
decl_stmt|;
comment|// Wrap with PacketLineIn to read ACK bytes from output stream
name|PacketLineIn
name|in
init|=
operator|new
name|PacketLineIn
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|String
name|tmp
init|=
name|in
operator|.
name|readString
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|tmp
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"ACK"
argument_list|)
expr_stmt|;
name|tmp
operator|=
name|in
operator|.
name|readString
argument_list|()
expr_stmt|;
comment|// Skip length (4 bytes) + 1 byte
comment|// to position the output stream to the raw zip stream
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|5
index|]
decl_stmt|;
name|IO
operator|.
name|readFully
argument_list|(
name|out
argument_list|,
name|buffer
argument_list|,
literal|0
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|entryNames
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
try|try
init|(
name|ZipArchiveInputStream
name|zip
init|=
operator|new
name|ZipArchiveInputStream
argument_list|(
name|out
argument_list|)
init|)
block|{
name|ZipArchiveEntry
name|zipEntry
init|=
name|zip
operator|.
name|getNextZipEntry
argument_list|()
decl_stmt|;
while|while
condition|(
name|zipEntry
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|zipEntry
operator|.
name|getName
argument_list|()
decl_stmt|;
name|entryNames
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|zipEntry
operator|=
name|zip
operator|.
name|getNextZipEntry
argument_list|()
expr_stmt|;
block|}
block|}
name|assertThat
argument_list|(
name|entryNames
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|entryNames
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s/%s"
argument_list|,
name|abbreviated
argument_list|,
name|PushOneCommit
operator|.
name|FILE_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|command (PushOneCommit.Result r, String abbreviated)
specifier|private
name|String
name|command
parameter_list|(
name|PushOneCommit
operator|.
name|Result
name|r
parameter_list|,
name|String
name|abbreviated
parameter_list|)
block|{
name|String
name|c
init|=
literal|"-f=zip "
operator|+
literal|"-9 "
operator|+
literal|"--prefix="
operator|+
name|abbreviated
operator|+
literal|"/ "
operator|+
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
operator|+
literal|" "
operator|+
name|PushOneCommit
operator|.
name|FILE_NAME
decl_stmt|;
return|return
name|c
return|;
block|}
DECL|method|archiveNotPermitted ()
specifier|private
name|void
name|archiveNotPermitted
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|String
name|abbreviated
init|=
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|abbreviate
argument_list|(
literal|8
argument_list|)
operator|.
name|name
argument_list|()
decl_stmt|;
name|String
name|c
init|=
name|command
argument_list|(
name|r
argument_list|,
name|abbreviated
argument_list|)
decl_stmt|;
name|InputStream
name|out
init|=
name|adminSshSession
operator|.
name|exec2
argument_list|(
literal|"git-upload-archive "
operator|+
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|argumentsToInputStream
argument_list|(
name|c
argument_list|)
argument_list|)
decl_stmt|;
comment|// Wrap with PacketLineIn to read ACK bytes from output stream
name|PacketLineIn
name|in
init|=
operator|new
name|PacketLineIn
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|String
name|tmp
init|=
name|in
operator|.
name|readString
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|tmp
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"ACK"
argument_list|)
expr_stmt|;
name|tmp
operator|=
name|in
operator|.
name|readString
argument_list|()
expr_stmt|;
name|tmp
operator|=
name|in
operator|.
name|readString
argument_list|()
expr_stmt|;
name|tmp
operator|=
name|tmp
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|tmp
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"fatal: upload-archive not permitted"
argument_list|)
expr_stmt|;
block|}
DECL|method|argumentsToInputStream (String c)
specifier|private
name|InputStream
name|argumentsToInputStream
parameter_list|(
name|String
name|c
parameter_list|)
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PacketLineOut
name|pctOut
init|=
operator|new
name|PacketLineOut
argument_list|(
name|out
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|arg
range|:
name|Splitter
operator|.
name|on
argument_list|(
literal|' '
argument_list|)
operator|.
name|split
argument_list|(
name|c
argument_list|)
control|)
block|{
name|pctOut
operator|.
name|writeString
argument_list|(
literal|"argument "
operator|+
name|arg
argument_list|)
expr_stmt|;
block|}
name|pctOut
operator|.
name|end
argument_list|()
expr_stmt|;
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

