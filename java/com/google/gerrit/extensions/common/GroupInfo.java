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
name|sql
operator|.
name|Timestamp
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

begin_class
DECL|class|GroupInfo
specifier|public
class|class
name|GroupInfo
extends|extends
name|GroupBaseInfo
block|{
DECL|field|url
specifier|public
name|String
name|url
decl_stmt|;
DECL|field|options
specifier|public
name|GroupOptionsInfo
name|options
decl_stmt|;
comment|// These fields are only supplied for internal groups.
DECL|field|description
specifier|public
name|String
name|description
decl_stmt|;
DECL|field|groupId
specifier|public
name|Integer
name|groupId
decl_stmt|;
DECL|field|owner
annotation|@
name|Deprecated
specifier|public
name|String
name|owner
decl_stmt|;
DECL|field|ownerId
annotation|@
name|Deprecated
specifier|public
name|String
name|ownerId
decl_stmt|;
DECL|field|createdOn
specifier|public
name|Timestamp
name|createdOn
decl_stmt|;
DECL|field|_moreGroups
specifier|public
name|Boolean
name|_moreGroups
decl_stmt|;
comment|// These fields are only supplied for internal groups, and only if requested.
DECL|field|members
specifier|public
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|members
decl_stmt|;
DECL|field|includes
specifier|public
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|includes
decl_stmt|;
block|}
end_class

end_unit

