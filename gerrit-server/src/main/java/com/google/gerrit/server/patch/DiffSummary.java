begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|readBytes
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|readString
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|writeBytes
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|writeString
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
name|Joiner
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|DeflaterOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|InflaterInputStream
import|;
end_import

begin_class
DECL|class|DiffSummary
specifier|public
class|class
name|DiffSummary
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
name|PatchListKey
operator|.
name|serialVersionUID
decl_stmt|;
DECL|field|paths
specifier|private
specifier|transient
name|String
index|[]
name|paths
decl_stmt|;
DECL|method|DiffSummary (String[] paths)
specifier|public
name|DiffSummary
parameter_list|(
name|String
index|[]
name|paths
parameter_list|)
block|{
name|this
operator|.
name|paths
operator|=
name|paths
expr_stmt|;
block|}
DECL|method|getPaths ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPaths
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|paths
argument_list|)
argument_list|)
return|;
block|}
DECL|method|writeObject (ObjectOutputStream output)
specifier|private
name|void
name|writeObject
parameter_list|(
name|ObjectOutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|buf
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
init|(
name|DeflaterOutputStream
name|out
init|=
operator|new
name|DeflaterOutputStream
argument_list|(
name|buf
argument_list|)
init|)
block|{
name|writeString
argument_list|(
name|out
argument_list|,
name|Joiner
operator|.
name|on
argument_list|(
literal|'\n'
argument_list|)
operator|.
name|join
argument_list|(
name|paths
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|writeBytes
argument_list|(
name|output
argument_list|,
name|buf
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|readObject (ObjectInputStream input)
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayInputStream
name|buf
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|readBytes
argument_list|(
name|input
argument_list|)
argument_list|)
decl_stmt|;
try|try
init|(
name|InflaterInputStream
name|in
init|=
operator|new
name|InflaterInputStream
argument_list|(
name|buf
argument_list|)
init|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|l
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|'\n'
argument_list|)
operator|.
name|splitToList
argument_list|(
name|readString
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|paths
operator|=
name|l
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|l
operator|.
name|size
argument_list|()
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

