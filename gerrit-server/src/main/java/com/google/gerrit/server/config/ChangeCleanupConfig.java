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
name|common
operator|.
name|base
operator|.
name|Strings
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
name|lib
operator|.
name|Config
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

begin_class
annotation|@
name|Singleton
DECL|class|ChangeCleanupConfig
specifier|public
class|class
name|ChangeCleanupConfig
block|{
DECL|field|SECTION
specifier|private
specifier|static
name|String
name|SECTION
init|=
literal|"changeCleanup"
decl_stmt|;
DECL|field|KEY_ABANDON_AFTER
specifier|private
specifier|static
name|String
name|KEY_ABANDON_AFTER
init|=
literal|"abandonAfter"
decl_stmt|;
DECL|field|KEY_ABANDON_MESSAGE
specifier|private
specifier|static
name|String
name|KEY_ABANDON_MESSAGE
init|=
literal|"abandonMessage"
decl_stmt|;
DECL|field|DEFAULT_ABANDON_MESSAGE
specifier|private
specifier|static
name|String
name|DEFAULT_ABANDON_MESSAGE
init|=
literal|"Auto-Abandoned due to inactivity, see "
operator|+
literal|"${URL}Documentation/user-change-cleanup.html#auto-abandon\n"
operator|+
literal|"\n"
operator|+
literal|"If this change is still wanted it should be restored."
decl_stmt|;
DECL|field|scheduleConfig
specifier|private
specifier|final
name|ScheduleConfig
name|scheduleConfig
decl_stmt|;
DECL|field|abandonAfter
specifier|private
specifier|final
name|long
name|abandonAfter
decl_stmt|;
DECL|field|abandonMessage
specifier|private
specifier|final
name|String
name|abandonMessage
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeCleanupConfig (@erritServerConfig Config cfg, @CanonicalWebUrl String canonicalWebUrl)
name|ChangeCleanupConfig
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
annotation|@
name|CanonicalWebUrl
name|String
name|canonicalWebUrl
parameter_list|)
block|{
name|scheduleConfig
operator|=
operator|new
name|ScheduleConfig
argument_list|(
name|cfg
argument_list|,
name|SECTION
argument_list|)
expr_stmt|;
name|abandonAfter
operator|=
name|readAbandonAfter
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|abandonMessage
operator|=
name|readAbandonMessage
argument_list|(
name|cfg
argument_list|,
name|canonicalWebUrl
argument_list|)
expr_stmt|;
block|}
DECL|method|readAbandonAfter (Config cfg)
specifier|private
name|long
name|readAbandonAfter
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
name|long
name|abandonAfter
init|=
name|ConfigUtil
operator|.
name|getTimeUnit
argument_list|(
name|cfg
argument_list|,
name|SECTION
argument_list|,
literal|null
argument_list|,
name|KEY_ABANDON_AFTER
argument_list|,
literal|0
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
return|return
name|abandonAfter
operator|>=
literal|0
condition|?
name|abandonAfter
else|:
literal|0
return|;
block|}
DECL|method|readAbandonMessage (Config cfg, String webUrl)
specifier|private
name|String
name|readAbandonMessage
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|webUrl
parameter_list|)
block|{
name|String
name|abandonMessage
init|=
name|cfg
operator|.
name|getString
argument_list|(
name|SECTION
argument_list|,
literal|null
argument_list|,
name|KEY_ABANDON_MESSAGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|abandonMessage
argument_list|)
condition|)
block|{
name|abandonMessage
operator|=
name|DEFAULT_ABANDON_MESSAGE
expr_stmt|;
block|}
name|abandonMessage
operator|=
name|abandonMessage
operator|.
name|replaceAll
argument_list|(
literal|"\\$\\{URL\\}"
argument_list|,
name|webUrl
argument_list|)
expr_stmt|;
return|return
name|abandonMessage
return|;
block|}
DECL|method|getScheduleConfig ()
specifier|public
name|ScheduleConfig
name|getScheduleConfig
parameter_list|()
block|{
return|return
name|scheduleConfig
return|;
block|}
DECL|method|getAbandonAfter ()
specifier|public
name|long
name|getAbandonAfter
parameter_list|()
block|{
return|return
name|abandonAfter
return|;
block|}
DECL|method|getAbandonMessage ()
specifier|public
name|String
name|getAbandonMessage
parameter_list|()
block|{
return|return
name|abandonMessage
return|;
block|}
block|}
end_class

end_unit

