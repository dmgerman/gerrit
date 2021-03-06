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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|ActionVisitor
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
name|config
operator|.
name|DownloadScheme
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
name|events
operator|.
name|AccountIndexedListener
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
name|events
operator|.
name|ChangeIndexedListener
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
name|events
operator|.
name|CommentAddedListener
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
name|events
operator|.
name|GitReferenceUpdatedListener
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
name|events
operator|.
name|GroupIndexedListener
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
name|events
operator|.
name|ProjectIndexedListener
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
name|events
operator|.
name|RevisionCreatedListener
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
name|registration
operator|.
name|DynamicMap
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
name|registration
operator|.
name|DynamicSet
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
name|registration
operator|.
name|PrivateInternals_DynamicMapImpl
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
name|registration
operator|.
name|RegistrationHandle
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
name|webui
operator|.
name|FileHistoryWebLink
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
name|webui
operator|.
name|PatchSetWebLink
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
name|ExceptionHook
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
name|GroupBackend
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
name|change
operator|.
name|ChangeETagComputation
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
name|ChangeMessageModifier
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
name|validators
operator|.
name|CommitValidationListener
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
name|validators
operator|.
name|OnSubmitValidationListener
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
name|validators
operator|.
name|RefOperationValidationListener
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
name|logging
operator|.
name|PerformanceLogger
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
name|rules
operator|.
name|SubmitRule
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
name|validators
operator|.
name|AccountActivationValidationListener
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
name|validators
operator|.
name|ProjectCreationValidationListener
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
name|util
operator|.
name|Providers
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
name|List
import|;
end_import

begin_class
DECL|class|ExtensionRegistry
specifier|public
class|class
name|ExtensionRegistry
block|{
DECL|field|accountIndexedListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|AccountIndexedListener
argument_list|>
name|accountIndexedListeners
decl_stmt|;
DECL|field|changeIndexedListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|ChangeIndexedListener
argument_list|>
name|changeIndexedListeners
decl_stmt|;
DECL|field|groupIndexedListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|GroupIndexedListener
argument_list|>
name|groupIndexedListeners
decl_stmt|;
DECL|field|projectIndexedListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|ProjectIndexedListener
argument_list|>
name|projectIndexedListeners
decl_stmt|;
DECL|field|commitValidationListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|CommitValidationListener
argument_list|>
name|commitValidationListeners
decl_stmt|;
DECL|field|exceptionHooks
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|ExceptionHook
argument_list|>
name|exceptionHooks
decl_stmt|;
DECL|field|performanceLoggers
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|PerformanceLogger
argument_list|>
name|performanceLoggers
decl_stmt|;
DECL|field|projectCreationValidationListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|ProjectCreationValidationListener
argument_list|>
name|projectCreationValidationListeners
decl_stmt|;
DECL|field|submitRules
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|SubmitRule
argument_list|>
name|submitRules
decl_stmt|;
DECL|field|changeMessageModifiers
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|ChangeMessageModifier
argument_list|>
name|changeMessageModifiers
decl_stmt|;
DECL|field|changeETagComputations
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|ChangeETagComputation
argument_list|>
name|changeETagComputations
decl_stmt|;
DECL|field|actionVisitors
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|ActionVisitor
argument_list|>
name|actionVisitors
decl_stmt|;
DECL|field|downloadSchemes
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|DownloadScheme
argument_list|>
name|downloadSchemes
decl_stmt|;
DECL|field|refOperationValidationListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|RefOperationValidationListener
argument_list|>
name|refOperationValidationListeners
decl_stmt|;
DECL|field|commentAddedListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|CommentAddedListener
argument_list|>
name|commentAddedListeners
decl_stmt|;
DECL|field|refUpdatedListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|GitReferenceUpdatedListener
argument_list|>
name|refUpdatedListeners
decl_stmt|;
DECL|field|fileHistoryWebLinks
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|FileHistoryWebLink
argument_list|>
name|fileHistoryWebLinks
decl_stmt|;
DECL|field|patchSetWebLinks
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|PatchSetWebLink
argument_list|>
name|patchSetWebLinks
decl_stmt|;
DECL|field|revisionCreatedListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|RevisionCreatedListener
argument_list|>
name|revisionCreatedListeners
decl_stmt|;
DECL|field|groupBackends
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|GroupBackend
argument_list|>
name|groupBackends
decl_stmt|;
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|AccountActivationValidationListener
argument_list|>
DECL|field|accountActivationValidationListeners
name|accountActivationValidationListeners
decl_stmt|;
DECL|field|onSubmitValidationListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|OnSubmitValidationListener
argument_list|>
name|onSubmitValidationListeners
decl_stmt|;
annotation|@
name|Inject
DECL|method|ExtensionRegistry ( DynamicSet<AccountIndexedListener> accountIndexedListeners, DynamicSet<ChangeIndexedListener> changeIndexedListeners, DynamicSet<GroupIndexedListener> groupIndexedListeners, DynamicSet<ProjectIndexedListener> projectIndexedListeners, DynamicSet<CommitValidationListener> commitValidationListeners, DynamicSet<ExceptionHook> exceptionHooks, DynamicSet<PerformanceLogger> performanceLoggers, DynamicSet<ProjectCreationValidationListener> projectCreationValidationListeners, DynamicSet<SubmitRule> submitRules, DynamicSet<ChangeMessageModifier> changeMessageModifiers, DynamicSet<ChangeETagComputation> changeETagComputations, DynamicSet<ActionVisitor> actionVisitors, DynamicMap<DownloadScheme> downloadSchemes, DynamicSet<RefOperationValidationListener> refOperationValidationListeners, DynamicSet<CommentAddedListener> commentAddedListeners, DynamicSet<GitReferenceUpdatedListener> refUpdatedListeners, DynamicSet<FileHistoryWebLink> fileHistoryWebLinks, DynamicSet<PatchSetWebLink> patchSetWebLinks, DynamicSet<RevisionCreatedListener> revisionCreatedListeners, DynamicSet<GroupBackend> groupBackends, DynamicSet<AccountActivationValidationListener> accountActivationValidationListeners, DynamicSet<OnSubmitValidationListener> onSubmitValidationListeners)
name|ExtensionRegistry
parameter_list|(
name|DynamicSet
argument_list|<
name|AccountIndexedListener
argument_list|>
name|accountIndexedListeners
parameter_list|,
name|DynamicSet
argument_list|<
name|ChangeIndexedListener
argument_list|>
name|changeIndexedListeners
parameter_list|,
name|DynamicSet
argument_list|<
name|GroupIndexedListener
argument_list|>
name|groupIndexedListeners
parameter_list|,
name|DynamicSet
argument_list|<
name|ProjectIndexedListener
argument_list|>
name|projectIndexedListeners
parameter_list|,
name|DynamicSet
argument_list|<
name|CommitValidationListener
argument_list|>
name|commitValidationListeners
parameter_list|,
name|DynamicSet
argument_list|<
name|ExceptionHook
argument_list|>
name|exceptionHooks
parameter_list|,
name|DynamicSet
argument_list|<
name|PerformanceLogger
argument_list|>
name|performanceLoggers
parameter_list|,
name|DynamicSet
argument_list|<
name|ProjectCreationValidationListener
argument_list|>
name|projectCreationValidationListeners
parameter_list|,
name|DynamicSet
argument_list|<
name|SubmitRule
argument_list|>
name|submitRules
parameter_list|,
name|DynamicSet
argument_list|<
name|ChangeMessageModifier
argument_list|>
name|changeMessageModifiers
parameter_list|,
name|DynamicSet
argument_list|<
name|ChangeETagComputation
argument_list|>
name|changeETagComputations
parameter_list|,
name|DynamicSet
argument_list|<
name|ActionVisitor
argument_list|>
name|actionVisitors
parameter_list|,
name|DynamicMap
argument_list|<
name|DownloadScheme
argument_list|>
name|downloadSchemes
parameter_list|,
name|DynamicSet
argument_list|<
name|RefOperationValidationListener
argument_list|>
name|refOperationValidationListeners
parameter_list|,
name|DynamicSet
argument_list|<
name|CommentAddedListener
argument_list|>
name|commentAddedListeners
parameter_list|,
name|DynamicSet
argument_list|<
name|GitReferenceUpdatedListener
argument_list|>
name|refUpdatedListeners
parameter_list|,
name|DynamicSet
argument_list|<
name|FileHistoryWebLink
argument_list|>
name|fileHistoryWebLinks
parameter_list|,
name|DynamicSet
argument_list|<
name|PatchSetWebLink
argument_list|>
name|patchSetWebLinks
parameter_list|,
name|DynamicSet
argument_list|<
name|RevisionCreatedListener
argument_list|>
name|revisionCreatedListeners
parameter_list|,
name|DynamicSet
argument_list|<
name|GroupBackend
argument_list|>
name|groupBackends
parameter_list|,
name|DynamicSet
argument_list|<
name|AccountActivationValidationListener
argument_list|>
name|accountActivationValidationListeners
parameter_list|,
name|DynamicSet
argument_list|<
name|OnSubmitValidationListener
argument_list|>
name|onSubmitValidationListeners
parameter_list|)
block|{
name|this
operator|.
name|accountIndexedListeners
operator|=
name|accountIndexedListeners
expr_stmt|;
name|this
operator|.
name|changeIndexedListeners
operator|=
name|changeIndexedListeners
expr_stmt|;
name|this
operator|.
name|groupIndexedListeners
operator|=
name|groupIndexedListeners
expr_stmt|;
name|this
operator|.
name|projectIndexedListeners
operator|=
name|projectIndexedListeners
expr_stmt|;
name|this
operator|.
name|commitValidationListeners
operator|=
name|commitValidationListeners
expr_stmt|;
name|this
operator|.
name|exceptionHooks
operator|=
name|exceptionHooks
expr_stmt|;
name|this
operator|.
name|performanceLoggers
operator|=
name|performanceLoggers
expr_stmt|;
name|this
operator|.
name|projectCreationValidationListeners
operator|=
name|projectCreationValidationListeners
expr_stmt|;
name|this
operator|.
name|submitRules
operator|=
name|submitRules
expr_stmt|;
name|this
operator|.
name|changeMessageModifiers
operator|=
name|changeMessageModifiers
expr_stmt|;
name|this
operator|.
name|changeETagComputations
operator|=
name|changeETagComputations
expr_stmt|;
name|this
operator|.
name|actionVisitors
operator|=
name|actionVisitors
expr_stmt|;
name|this
operator|.
name|downloadSchemes
operator|=
name|downloadSchemes
expr_stmt|;
name|this
operator|.
name|refOperationValidationListeners
operator|=
name|refOperationValidationListeners
expr_stmt|;
name|this
operator|.
name|commentAddedListeners
operator|=
name|commentAddedListeners
expr_stmt|;
name|this
operator|.
name|refUpdatedListeners
operator|=
name|refUpdatedListeners
expr_stmt|;
name|this
operator|.
name|fileHistoryWebLinks
operator|=
name|fileHistoryWebLinks
expr_stmt|;
name|this
operator|.
name|patchSetWebLinks
operator|=
name|patchSetWebLinks
expr_stmt|;
name|this
operator|.
name|revisionCreatedListeners
operator|=
name|revisionCreatedListeners
expr_stmt|;
name|this
operator|.
name|groupBackends
operator|=
name|groupBackends
expr_stmt|;
name|this
operator|.
name|accountActivationValidationListeners
operator|=
name|accountActivationValidationListeners
expr_stmt|;
name|this
operator|.
name|onSubmitValidationListeners
operator|=
name|onSubmitValidationListeners
expr_stmt|;
block|}
DECL|method|newRegistration ()
specifier|public
name|Registration
name|newRegistration
parameter_list|()
block|{
return|return
operator|new
name|Registration
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"FunctionalInterfaceClash"
argument_list|)
DECL|class|Registration
specifier|public
class|class
name|Registration
implements|implements
name|AutoCloseable
block|{
DECL|field|registrationHandles
specifier|private
specifier|final
name|List
argument_list|<
name|RegistrationHandle
argument_list|>
name|registrationHandles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|add (AccountIndexedListener accountIndexedListener)
specifier|public
name|Registration
name|add
parameter_list|(
name|AccountIndexedListener
name|accountIndexedListener
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|accountIndexedListeners
argument_list|,
name|accountIndexedListener
argument_list|)
return|;
block|}
DECL|method|add (ChangeIndexedListener changeIndexedListener)
specifier|public
name|Registration
name|add
parameter_list|(
name|ChangeIndexedListener
name|changeIndexedListener
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|changeIndexedListeners
argument_list|,
name|changeIndexedListener
argument_list|)
return|;
block|}
DECL|method|add (GroupIndexedListener groupIndexedListener)
specifier|public
name|Registration
name|add
parameter_list|(
name|GroupIndexedListener
name|groupIndexedListener
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|groupIndexedListeners
argument_list|,
name|groupIndexedListener
argument_list|)
return|;
block|}
DECL|method|add (ProjectIndexedListener projectIndexedListener)
specifier|public
name|Registration
name|add
parameter_list|(
name|ProjectIndexedListener
name|projectIndexedListener
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|projectIndexedListeners
argument_list|,
name|projectIndexedListener
argument_list|)
return|;
block|}
DECL|method|add (CommitValidationListener commitValidationListener)
specifier|public
name|Registration
name|add
parameter_list|(
name|CommitValidationListener
name|commitValidationListener
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|commitValidationListeners
argument_list|,
name|commitValidationListener
argument_list|)
return|;
block|}
DECL|method|add (ExceptionHook exceptionHook)
specifier|public
name|Registration
name|add
parameter_list|(
name|ExceptionHook
name|exceptionHook
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|exceptionHooks
argument_list|,
name|exceptionHook
argument_list|)
return|;
block|}
DECL|method|add (PerformanceLogger performanceLogger)
specifier|public
name|Registration
name|add
parameter_list|(
name|PerformanceLogger
name|performanceLogger
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|performanceLoggers
argument_list|,
name|performanceLogger
argument_list|)
return|;
block|}
DECL|method|add (ProjectCreationValidationListener projectCreationListener)
specifier|public
name|Registration
name|add
parameter_list|(
name|ProjectCreationValidationListener
name|projectCreationListener
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|projectCreationValidationListeners
argument_list|,
name|projectCreationListener
argument_list|)
return|;
block|}
DECL|method|add (SubmitRule submitRule)
specifier|public
name|Registration
name|add
parameter_list|(
name|SubmitRule
name|submitRule
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|submitRules
argument_list|,
name|submitRule
argument_list|)
return|;
block|}
DECL|method|add (ChangeMessageModifier changeMessageModifier)
specifier|public
name|Registration
name|add
parameter_list|(
name|ChangeMessageModifier
name|changeMessageModifier
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|changeMessageModifiers
argument_list|,
name|changeMessageModifier
argument_list|)
return|;
block|}
DECL|method|add (ChangeMessageModifier changeMessageModifier, String exportName)
specifier|public
name|Registration
name|add
parameter_list|(
name|ChangeMessageModifier
name|changeMessageModifier
parameter_list|,
name|String
name|exportName
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|changeMessageModifiers
argument_list|,
name|changeMessageModifier
argument_list|,
name|exportName
argument_list|)
return|;
block|}
DECL|method|add (ChangeETagComputation changeETagComputation)
specifier|public
name|Registration
name|add
parameter_list|(
name|ChangeETagComputation
name|changeETagComputation
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|changeETagComputations
argument_list|,
name|changeETagComputation
argument_list|)
return|;
block|}
DECL|method|add (ActionVisitor actionVisitor)
specifier|public
name|Registration
name|add
parameter_list|(
name|ActionVisitor
name|actionVisitor
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|actionVisitors
argument_list|,
name|actionVisitor
argument_list|)
return|;
block|}
DECL|method|add (DownloadScheme downloadScheme, String exportName)
specifier|public
name|Registration
name|add
parameter_list|(
name|DownloadScheme
name|downloadScheme
parameter_list|,
name|String
name|exportName
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|downloadSchemes
argument_list|,
name|downloadScheme
argument_list|,
name|exportName
argument_list|)
return|;
block|}
DECL|method|add (RefOperationValidationListener refOperationValidationListener)
specifier|public
name|Registration
name|add
parameter_list|(
name|RefOperationValidationListener
name|refOperationValidationListener
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|refOperationValidationListeners
argument_list|,
name|refOperationValidationListener
argument_list|)
return|;
block|}
DECL|method|add (CommentAddedListener commentAddedListener)
specifier|public
name|Registration
name|add
parameter_list|(
name|CommentAddedListener
name|commentAddedListener
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|commentAddedListeners
argument_list|,
name|commentAddedListener
argument_list|)
return|;
block|}
DECL|method|add (GitReferenceUpdatedListener refUpdatedListener)
specifier|public
name|Registration
name|add
parameter_list|(
name|GitReferenceUpdatedListener
name|refUpdatedListener
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|refUpdatedListeners
argument_list|,
name|refUpdatedListener
argument_list|)
return|;
block|}
DECL|method|add (FileHistoryWebLink fileHistoryWebLink)
specifier|public
name|Registration
name|add
parameter_list|(
name|FileHistoryWebLink
name|fileHistoryWebLink
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|fileHistoryWebLinks
argument_list|,
name|fileHistoryWebLink
argument_list|)
return|;
block|}
DECL|method|add (PatchSetWebLink patchSetWebLink)
specifier|public
name|Registration
name|add
parameter_list|(
name|PatchSetWebLink
name|patchSetWebLink
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|patchSetWebLinks
argument_list|,
name|patchSetWebLink
argument_list|)
return|;
block|}
DECL|method|add (RevisionCreatedListener revisionCreatedListener)
specifier|public
name|Registration
name|add
parameter_list|(
name|RevisionCreatedListener
name|revisionCreatedListener
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|revisionCreatedListeners
argument_list|,
name|revisionCreatedListener
argument_list|)
return|;
block|}
DECL|method|add (GroupBackend groupBackend)
specifier|public
name|Registration
name|add
parameter_list|(
name|GroupBackend
name|groupBackend
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|groupBackends
argument_list|,
name|groupBackend
argument_list|)
return|;
block|}
DECL|method|add ( AccountActivationValidationListener accountActivationValidationListener)
specifier|public
name|Registration
name|add
parameter_list|(
name|AccountActivationValidationListener
name|accountActivationValidationListener
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|accountActivationValidationListeners
argument_list|,
name|accountActivationValidationListener
argument_list|)
return|;
block|}
DECL|method|add (OnSubmitValidationListener onSubmitValidationListener)
specifier|public
name|Registration
name|add
parameter_list|(
name|OnSubmitValidationListener
name|onSubmitValidationListener
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|onSubmitValidationListeners
argument_list|,
name|onSubmitValidationListener
argument_list|)
return|;
block|}
DECL|method|add (DynamicSet<T> dynamicSet, T extension)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|Registration
name|add
parameter_list|(
name|DynamicSet
argument_list|<
name|T
argument_list|>
name|dynamicSet
parameter_list|,
name|T
name|extension
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|dynamicSet
argument_list|,
name|extension
argument_list|,
literal|"gerrit"
argument_list|)
return|;
block|}
DECL|method|add (DynamicSet<T> dynamicSet, T extension, String exportname)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|Registration
name|add
parameter_list|(
name|DynamicSet
argument_list|<
name|T
argument_list|>
name|dynamicSet
parameter_list|,
name|T
name|extension
parameter_list|,
name|String
name|exportname
parameter_list|)
block|{
name|RegistrationHandle
name|registrationHandle
init|=
name|dynamicSet
operator|.
name|add
argument_list|(
name|exportname
argument_list|,
name|extension
argument_list|)
decl_stmt|;
name|registrationHandles
operator|.
name|add
argument_list|(
name|registrationHandle
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|add (DynamicMap<T> dynamicMap, T extension, String exportName)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|Registration
name|add
parameter_list|(
name|DynamicMap
argument_list|<
name|T
argument_list|>
name|dynamicMap
parameter_list|,
name|T
name|extension
parameter_list|,
name|String
name|exportName
parameter_list|)
block|{
name|RegistrationHandle
name|registrationHandle
init|=
operator|(
operator|(
name|PrivateInternals_DynamicMapImpl
argument_list|<
name|T
argument_list|>
operator|)
name|dynamicMap
operator|)
operator|.
name|put
argument_list|(
literal|"myPlugin"
argument_list|,
name|exportName
argument_list|,
name|Providers
operator|.
name|of
argument_list|(
name|extension
argument_list|)
argument_list|)
decl_stmt|;
name|registrationHandles
operator|.
name|add
argument_list|(
name|registrationHandle
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|registrationHandles
operator|.
name|forEach
argument_list|(
name|h
lambda|->
name|h
operator|.
name|remove
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

