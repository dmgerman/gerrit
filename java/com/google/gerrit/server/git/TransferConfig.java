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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|config
operator|.
name|ConfigUtil
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
name|config
operator|.
name|GerritServerConfig
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|Config
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
name|storage
operator|.
name|pack
operator|.
name|PackConfig
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|TransferConfig
specifier|public
class|class
name|TransferConfig
block|{
DECL|field|timeout
specifier|private
specifier|final
name|int
name|timeout
decl_stmt|;
DECL|field|packConfig
specifier|private
specifier|final
name|PackConfig
name|packConfig
decl_stmt|;
DECL|field|maxObjectSizeLimit
specifier|private
specifier|final
name|long
name|maxObjectSizeLimit
decl_stmt|;
DECL|field|maxObjectSizeLimitFormatted
specifier|private
specifier|final
name|String
name|maxObjectSizeLimitFormatted
decl_stmt|;
DECL|field|inheritProjectMaxObjectSizeLimit
specifier|private
specifier|final
name|boolean
name|inheritProjectMaxObjectSizeLimit
decl_stmt|;
DECL|field|refPermissionBackend
specifier|private
specifier|final
name|RefPermissionBackend
name|refPermissionBackend
decl_stmt|;
DECL|field|enableProtocolV2
specifier|private
specifier|final
name|boolean
name|enableProtocolV2
decl_stmt|;
annotation|@
name|Inject
DECL|method|TransferConfig (@erritServerConfig Config cfg)
name|TransferConfig
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|timeout
operator|=
operator|(
name|int
operator|)
name|ConfigUtil
operator|.
name|getTimeUnit
argument_list|(
name|cfg
argument_list|,
literal|"transfer"
argument_list|,
literal|null
argument_list|,
literal|"timeout"
argument_list|,
comment|//
literal|0
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|maxObjectSizeLimit
operator|=
name|cfg
operator|.
name|getLong
argument_list|(
literal|"receive"
argument_list|,
literal|"maxObjectSizeLimit"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|maxObjectSizeLimitFormatted
operator|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"maxObjectSizeLimit"
argument_list|)
expr_stmt|;
name|inheritProjectMaxObjectSizeLimit
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"receive"
argument_list|,
literal|"inheritProjectMaxObjectSizeLimit"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|refPermissionBackend
operator|=
name|cfg
operator|.
name|getEnum
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"refPermissionBackend"
argument_list|,
name|RefPermissionBackend
operator|.
name|ADVERTISE_REF_HOOK
argument_list|)
expr_stmt|;
name|enableProtocolV2
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"receive"
argument_list|,
literal|"enableProtocolV2"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|packConfig
operator|=
operator|new
name|PackConfig
argument_list|()
expr_stmt|;
name|packConfig
operator|.
name|setDeltaCompress
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|packConfig
operator|.
name|setThreads
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|packConfig
operator|.
name|fromConfig
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
block|}
comment|/** @return configured timeout, in seconds. 0 if the timeout is infinite. */
DECL|method|getTimeout ()
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|getPackConfig ()
specifier|public
name|PackConfig
name|getPackConfig
parameter_list|()
block|{
return|return
name|packConfig
return|;
block|}
DECL|method|getMaxObjectSizeLimit ()
specifier|public
name|long
name|getMaxObjectSizeLimit
parameter_list|()
block|{
return|return
name|maxObjectSizeLimit
return|;
block|}
DECL|method|getFormattedMaxObjectSizeLimit ()
specifier|public
name|String
name|getFormattedMaxObjectSizeLimit
parameter_list|()
block|{
return|return
name|maxObjectSizeLimitFormatted
return|;
block|}
DECL|method|inheritProjectMaxObjectSizeLimit ()
specifier|public
name|boolean
name|inheritProjectMaxObjectSizeLimit
parameter_list|()
block|{
return|return
name|inheritProjectMaxObjectSizeLimit
return|;
block|}
DECL|method|getRefPermissionBackend ()
specifier|public
name|RefPermissionBackend
name|getRefPermissionBackend
parameter_list|()
block|{
return|return
name|refPermissionBackend
return|;
block|}
DECL|method|enableProtocolV2 ()
specifier|public
name|boolean
name|enableProtocolV2
parameter_list|()
block|{
return|return
name|enableProtocolV2
return|;
block|}
block|}
end_class

end_unit

