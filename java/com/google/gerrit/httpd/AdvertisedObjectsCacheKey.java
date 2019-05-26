begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|entities
operator|.
name|Account
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
name|entities
operator|.
name|Project
import|;
end_import

begin_class
annotation|@
name|AutoValue
DECL|class|AdvertisedObjectsCacheKey
specifier|abstract
class|class
name|AdvertisedObjectsCacheKey
block|{
DECL|method|create (Account.Id account, Project.NameKey project)
specifier|static
name|AdvertisedObjectsCacheKey
name|create
parameter_list|(
name|Account
operator|.
name|Id
name|account
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_AdvertisedObjectsCacheKey
argument_list|(
name|account
argument_list|,
name|project
argument_list|)
return|;
block|}
DECL|method|account ()
specifier|public
specifier|abstract
name|Account
operator|.
name|Id
name|account
parameter_list|()
function_decl|;
DECL|method|project ()
specifier|public
specifier|abstract
name|Project
operator|.
name|NameKey
name|project
parameter_list|()
function_decl|;
block|}
end_class

end_unit

