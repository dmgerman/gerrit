begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change.testing
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
operator|.
name|testing
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
operator|.
name|ChangeETagComputation
import|;
end_import

begin_class
DECL|class|TestChangeETagComputation
specifier|public
class|class
name|TestChangeETagComputation
block|{
DECL|method|withETag (String etag)
specifier|public
specifier|static
name|ChangeETagComputation
name|withETag
parameter_list|(
name|String
name|etag
parameter_list|)
block|{
return|return
parameter_list|(
name|p
parameter_list|,
name|id
parameter_list|)
lambda|->
name|etag
return|;
block|}
DECL|method|withException (RuntimeException e)
specifier|public
specifier|static
name|ChangeETagComputation
name|withException
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
return|return
parameter_list|(
name|p
parameter_list|,
name|id
parameter_list|)
lambda|->
block|{
throw|throw
name|e
throw|;
block|}
return|;
block|}
block|}
end_class

end_unit

