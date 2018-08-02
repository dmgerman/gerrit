begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_class
DECL|class|FileInfo
specifier|public
class|class
name|FileInfo
block|{
DECL|field|status
specifier|public
name|Character
name|status
decl_stmt|;
DECL|field|binary
specifier|public
name|Boolean
name|binary
decl_stmt|;
DECL|field|oldPath
specifier|public
name|String
name|oldPath
decl_stmt|;
DECL|field|linesInserted
specifier|public
name|Integer
name|linesInserted
decl_stmt|;
DECL|field|linesDeleted
specifier|public
name|Integer
name|linesDeleted
decl_stmt|;
DECL|field|sizeDelta
specifier|public
name|long
name|sizeDelta
decl_stmt|;
DECL|field|size
specifier|public
name|long
name|size
decl_stmt|;
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|FileInfo
condition|)
block|{
name|FileInfo
name|fileInfo
init|=
operator|(
name|FileInfo
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|status
argument_list|,
name|fileInfo
operator|.
name|status
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|binary
argument_list|,
name|fileInfo
operator|.
name|binary
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|oldPath
argument_list|,
name|fileInfo
operator|.
name|oldPath
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|linesInserted
argument_list|,
name|fileInfo
operator|.
name|linesInserted
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|linesDeleted
argument_list|,
name|fileInfo
operator|.
name|linesDeleted
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|sizeDelta
argument_list|,
name|fileInfo
operator|.
name|sizeDelta
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|size
argument_list|,
name|fileInfo
operator|.
name|size
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|status
argument_list|,
name|binary
argument_list|,
name|oldPath
argument_list|,
name|linesInserted
argument_list|,
name|linesDeleted
argument_list|,
name|sizeDelta
argument_list|,
name|size
argument_list|)
return|;
block|}
block|}
end_class

end_unit

