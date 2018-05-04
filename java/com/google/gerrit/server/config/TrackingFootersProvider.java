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
name|flogger
operator|.
name|FluentLogger
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
name|reviewdb
operator|.
name|client
operator|.
name|TrackingId
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
name|Provider
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
name|ArrayList
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
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|PatternSyntaxException
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

begin_comment
comment|/** Provides a list of all configured {@link TrackingFooter}s. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|TrackingFootersProvider
specifier|public
class|class
name|TrackingFootersProvider
implements|implements
name|Provider
argument_list|<
name|TrackingFooters
argument_list|>
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|TRACKING_ID_TAG
specifier|private
specifier|static
name|String
name|TRACKING_ID_TAG
init|=
literal|"trackingid"
decl_stmt|;
DECL|field|FOOTER_TAG
specifier|private
specifier|static
name|String
name|FOOTER_TAG
init|=
literal|"footer"
decl_stmt|;
DECL|field|SYSTEM_TAG
specifier|private
specifier|static
name|String
name|SYSTEM_TAG
init|=
literal|"system"
decl_stmt|;
DECL|field|REGEX_TAG
specifier|private
specifier|static
name|String
name|REGEX_TAG
init|=
literal|"match"
decl_stmt|;
DECL|field|trackingFooters
specifier|private
specifier|final
name|List
argument_list|<
name|TrackingFooter
argument_list|>
name|trackingFooters
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Inject
DECL|method|TrackingFootersProvider (@erritServerConfig Config cfg)
name|TrackingFootersProvider
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
for|for
control|(
name|String
name|name
range|:
name|cfg
operator|.
name|getSubsections
argument_list|(
name|TRACKING_ID_TAG
argument_list|)
control|)
block|{
name|boolean
name|configValid
init|=
literal|true
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|footers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|cfg
operator|.
name|getStringList
argument_list|(
name|TRACKING_ID_TAG
argument_list|,
name|name
argument_list|,
name|FOOTER_TAG
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|footers
operator|.
name|removeAll
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|footers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|configValid
operator|=
literal|false
expr_stmt|;
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"Missing %s.%s.%s in gerrit.config"
argument_list|,
name|TRACKING_ID_TAG
argument_list|,
name|name
argument_list|,
name|FOOTER_TAG
argument_list|)
expr_stmt|;
block|}
name|String
name|system
init|=
name|cfg
operator|.
name|getString
argument_list|(
name|TRACKING_ID_TAG
argument_list|,
name|name
argument_list|,
name|SYSTEM_TAG
argument_list|)
decl_stmt|;
if|if
condition|(
name|system
operator|==
literal|null
operator|||
name|system
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|configValid
operator|=
literal|false
expr_stmt|;
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"Missing %s.%s.%s in gerrit.config"
argument_list|,
name|TRACKING_ID_TAG
argument_list|,
name|name
argument_list|,
name|SYSTEM_TAG
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|system
operator|.
name|length
argument_list|()
operator|>
name|TrackingId
operator|.
name|TRACKING_SYSTEM_MAX_CHAR
condition|)
block|{
name|configValid
operator|=
literal|false
expr_stmt|;
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"String too long \"%s\" in gerrit.config %s.%s.%s (max %d char)"
argument_list|,
name|system
argument_list|,
name|TRACKING_ID_TAG
argument_list|,
name|name
argument_list|,
name|SYSTEM_TAG
argument_list|,
name|TrackingId
operator|.
name|TRACKING_SYSTEM_MAX_CHAR
argument_list|)
expr_stmt|;
block|}
name|String
name|match
init|=
name|cfg
operator|.
name|getString
argument_list|(
name|TRACKING_ID_TAG
argument_list|,
name|name
argument_list|,
name|REGEX_TAG
argument_list|)
decl_stmt|;
if|if
condition|(
name|match
operator|==
literal|null
operator|||
name|match
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|configValid
operator|=
literal|false
expr_stmt|;
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"Missing %s.%s.%s in gerrit.config"
argument_list|,
name|TRACKING_ID_TAG
argument_list|,
name|name
argument_list|,
name|REGEX_TAG
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configValid
condition|)
block|{
try|try
block|{
for|for
control|(
name|String
name|footer
range|:
name|footers
control|)
block|{
name|trackingFooters
operator|.
name|add
argument_list|(
operator|new
name|TrackingFooter
argument_list|(
name|footer
argument_list|,
name|match
argument_list|,
name|system
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|PatternSyntaxException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"Invalid pattern \"%s\" in gerrit.config %s.%s.%s: %s"
argument_list|,
name|match
argument_list|,
name|TRACKING_ID_TAG
argument_list|,
name|name
argument_list|,
name|REGEX_TAG
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|TrackingFooters
name|get
parameter_list|()
block|{
return|return
operator|new
name|TrackingFooters
argument_list|(
name|trackingFooters
argument_list|)
return|;
block|}
block|}
end_class

end_unit

