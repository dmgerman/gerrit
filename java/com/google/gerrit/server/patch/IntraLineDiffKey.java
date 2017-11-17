begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|extensions
operator|.
name|client
operator|.
name|DiffPreferencesInfo
operator|.
name|Whitespace
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_class
annotation|@
name|AutoValue
DECL|class|IntraLineDiffKey
specifier|public
specifier|abstract
class|class
name|IntraLineDiffKey
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|public
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|8L
decl_stmt|;
DECL|method|create (ObjectId aId, ObjectId bId, Whitespace whitespace)
specifier|public
specifier|static
name|IntraLineDiffKey
name|create
parameter_list|(
name|ObjectId
name|aId
parameter_list|,
name|ObjectId
name|bId
parameter_list|,
name|Whitespace
name|whitespace
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_IntraLineDiffKey
argument_list|(
name|aId
argument_list|,
name|bId
argument_list|,
name|whitespace
argument_list|)
return|;
block|}
DECL|method|getBlobA ()
specifier|public
specifier|abstract
name|ObjectId
name|getBlobA
parameter_list|()
function_decl|;
DECL|method|getBlobB ()
specifier|public
specifier|abstract
name|ObjectId
name|getBlobB
parameter_list|()
function_decl|;
DECL|method|getWhitespace ()
specifier|public
specifier|abstract
name|Whitespace
name|getWhitespace
parameter_list|()
function_decl|;
block|}
end_class

end_unit

