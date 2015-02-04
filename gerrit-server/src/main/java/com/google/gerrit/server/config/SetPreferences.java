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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|common
operator|.
name|data
operator|.
name|GlobalCapability
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
name|annotations
operator|.
name|RequiresCapability
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
name|restapi
operator|.
name|BadRequestException
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
name|restapi
operator|.
name|RestModifyView
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
name|server
operator|.
name|account
operator|.
name|GetPreferences
operator|.
name|PreferenceInfo
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
name|server
operator|.
name|account
operator|.
name|SetPreferences
operator|.
name|Input
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
name|server
operator|.
name|account
operator|.
name|VersionedAccountPreferences
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
name|server
operator|.
name|git
operator|.
name|MetaDataUpdate
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|errors
operator|.
name|ConfigInvalidException
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

begin_class
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
annotation|@
name|Singleton
DECL|class|SetPreferences
specifier|public
class|class
name|SetPreferences
implements|implements
name|RestModifyView
argument_list|<
name|ConfigResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|field|metaDataUpdateFactory
specifier|private
specifier|final
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
annotation|@
name|Inject
DECL|method|SetPreferences (MetaDataUpdate.User metaDataUpdateFactory, AllUsersName allUsersName)
name|SetPreferences
parameter_list|(
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|)
block|{
name|this
operator|.
name|metaDataUpdateFactory
operator|=
name|metaDataUpdateFactory
expr_stmt|;
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ConfigResource rsrc, Input i)
specifier|public
name|Object
name|apply
parameter_list|(
name|ConfigResource
name|rsrc
parameter_list|,
name|Input
name|i
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|i
operator|.
name|changesPerPage
operator|!=
literal|null
operator|||
name|i
operator|.
name|showSiteHeader
operator|!=
literal|null
operator|||
name|i
operator|.
name|useFlashClipboard
operator|!=
literal|null
operator|||
name|i
operator|.
name|downloadScheme
operator|!=
literal|null
operator|||
name|i
operator|.
name|downloadCommand
operator|!=
literal|null
operator|||
name|i
operator|.
name|copySelfOnEmail
operator|!=
literal|null
operator|||
name|i
operator|.
name|dateFormat
operator|!=
literal|null
operator|||
name|i
operator|.
name|timeFormat
operator|!=
literal|null
operator|||
name|i
operator|.
name|relativeDateInChangeTable
operator|!=
literal|null
operator|||
name|i
operator|.
name|sizeBarInChangeTable
operator|!=
literal|null
operator|||
name|i
operator|.
name|legacycidInChangeTable
operator|!=
literal|null
operator|||
name|i
operator|.
name|muteCommonPathPrefixes
operator|!=
literal|null
operator|||
name|i
operator|.
name|reviewCategoryStrategy
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"unsupported option"
argument_list|)
throw|;
block|}
name|VersionedAccountPreferences
name|p
decl_stmt|;
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|allUsersName
argument_list|)
decl_stmt|;
try|try
block|{
name|p
operator|=
name|VersionedAccountPreferences
operator|.
name|forDefault
argument_list|()
expr_stmt|;
name|p
operator|.
name|load
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
operator|.
name|SetPreferences
operator|.
name|storeMyMenus
argument_list|(
name|p
argument_list|,
name|i
operator|.
name|my
argument_list|)
expr_stmt|;
name|p
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
return|return
operator|new
name|PreferenceInfo
argument_list|(
literal|null
argument_list|,
name|p
argument_list|,
name|md
operator|.
name|getRepository
argument_list|()
argument_list|)
return|;
block|}
finally|finally
block|{
name|md
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

